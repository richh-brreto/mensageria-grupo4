package com.boost.rabbit.infraestructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.boost.rabbit.infraestructure.usecase.EnviarNotificacaoWhatsAppUseCase;
import com.twilio.Twilio;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WhatsAppConsumer {

    public static final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    public static final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EnviarNotificacaoWhatsAppUseCase enviarNotificacaoUseCase;

    static {
        if (ACCOUNT_SID == null || ACCOUNT_SID.isBlank() || AUTH_TOKEN == null || AUTH_TOKEN.isBlank()) {
            throw new TwilioTokenNotConfiguredException(
                    "Twilio token não configurado. Defina as variáveis de ambiente TWILIO_ACCOUNT_SID e TWILIO_AUTH_TOKEN antes de iniciar a aplicação."
            );
        }

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    private static String readEnvOrProperty(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return value;
    }

    public WhatsAppConsumer(EnviarNotificacaoWhatsAppUseCase enviarNotificacaoUseCase) {
        this.enviarNotificacaoUseCase = enviarNotificacaoUseCase;
    }

    @RabbitListener(queues = "contratos.vencimento.queue")
    public void consumirMensagem(String mensagemJson) {
        try {
            System.out.println("[*] JSON recebido na mensageria: " + mensagemJson);

            JsonNode rootNode = objectMapper.readTree(mensagemJson);
            String telefone = rootNode.path("destinatario").path("telefone").asText();

            // Delega a regra para o UseCase
            enviarNotificacaoUseCase.executar(telefone);

        } catch (Exception e) {
            System.err.println("[X] Erro ao consumir mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}