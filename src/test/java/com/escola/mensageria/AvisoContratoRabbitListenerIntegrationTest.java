package com.escola.mensageria;

import com.escola.mensageria.domain.gateway.WhatsAppServicePort;
import com.escola.mensageria.usecase.WhatsAppServicePortSpy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.rabbitmq.RabbitMQContainer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class AvisoContratoRabbitListenerIntegrationTest {

    private static final String PAYLOAD = """
            {
              "contrato": { "id": 12, "tipo": "Grupo", "dataInicio": "2026-08-01", "dataFim": "2026-10-20" },
              "aluno": { "id": 1, "nome": "Guilherme Silva2", "email": "guilherme.silva@sptech.school", "telefone": "11977776666", "nível": "Intermediário", "ativo": true },
              "professor": null,
              "turma": { "id": 1, "nome": "ADS-2B", "nível": "Intermediário", "limiteAlunos": 30, "tipo": "Grupo", "professorId": 1 },
              "destinatario": { "id": 1, "nome": "Administrador Sistema", "email": "admin@sptech.school", "telefone": "1199999888", "ativo": true },
              "diasParaVencer": 18,
              "dataAviso": "2026-10-02",
              "publicadoEm": "2026-10-02T10:15:30"
            }
            """;

    @Container
    static final RabbitMQContainer RABBITMQ = new RabbitMQContainer("rabbitmq:3.13-management-alpine");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBITMQ::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ::getAdminPassword);
        registry.add("spring.rabbitmq.listener.simple.retry.enabled", () -> "false");
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WhatsAppServicePortSpy porteSpy;

    @Test
    @DisplayName("consome o payload real da fila e envia uma mensagem para o destinatario e outra para o aluno")
    void deveProcessarPayloadDaFila() throws InterruptedException {
        porteSpy.limpar();

        publicar(PAYLOAD);

        List<WhatsAppServicePortSpy.Envio> envios = aguardarEnvios(2);

        assertEquals("+551199999888", envios.get(0).destino().getValor());
        assertTrue(envios.get(0).mensagem().contains("Olá, Administrador Sistema!"));
        assertTrue(envios.get(0).mensagem().contains("faltam 18 dias"));
        assertTrue(envios.get(0).mensagem().contains("Turma: ADS-2B."));

        assertEquals("+5511977776666", envios.get(1).destino().getValor());
        assertTrue(envios.get(1).mensagem().contains("Olá, Guilherme Silva2!"));
        assertTrue(envios.get(1).mensagem().contains("Renove sua matrícula"));
    }

    @Test
    @DisplayName("processa o mesmo payload quando o professor vem preenchido")
    void deveAceitarProfessorPreenchido() throws InterruptedException {
        porteSpy.limpar();

        publicar(PAYLOAD.replace("\"professor\": null",
                "\"professor\": { \"id\": 1, \"nome\": \"Ricardo\", \"email\": \"ricardo@sptech.school\", "
                        + "\"telefone\": \"11955554444\", \"nível\": \"Intermediário\", \"ativo\": true }"));

        List<WhatsAppServicePortSpy.Envio> envios = aguardarEnvios(2);

        assertTrue(envios.get(0).mensagem().contains("Professor responsável: Ricardo."));
    }

    private void publicar(String json) {
        rabbitTemplate.send(
                "mensageria.aula.exchange",
                "mensageria.aula.routing",
                new Message(json.getBytes(StandardCharsets.UTF_8))
        );
    }

    private List<WhatsAppServicePortSpy.Envio> aguardarEnvios(int quantidade) throws InterruptedException {
        long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);
        while (porteSpy.envios().size() < quantidade && System.nanoTime() < limite) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
        List<WhatsAppServicePortSpy.Envio> envios = porteSpy.envios();
        assertEquals(quantidade, envios.size(), "Envios realizados: " + envios.size());
        return envios;
    }

    @TestConfiguration
    static class PorteFalsoConfig {

        @Bean
        @Primary
        WhatsAppServicePortSpy porteSpy() {
            return new WhatsAppServicePortSpy();
        }
    }
}
