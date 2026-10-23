package com.boost.rabbit.infraestructure.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ContratoVencimentoMessageProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getTelefone(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return root.path("aluno").path("telefone").asText();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String montarMensagem(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            String alunoNome = root.path("aluno").path("nome").asText();
            String contratoId = root.path("contrato").path("id").asText();
            String alunoNivel = root.path("aluno").path("nivel").asText();
            String contratoDataFim = root.path("contrato").path("dataFim").asText();
            String diasParaVencer = root.path("diasParaVencer").asText();

            return String.format(
                    "Olá, %s! 💙 Equipe Boost Your English por aqui!\n\n" +
                    "Identificamos que o seu contrato está na reta final e queremos garantir que você continue evoluindo no inglês sem interrupções.\n\n" +
                    "📌 Detalhes do seu contrato:\n" +
                    "• ID do Contrato: #%s\n" +
                    "• Nível Atual: %s\n" +
                    "• Data de Vencimento: %s\n" +
                    "• Prazo Restante: %s dias\n\n" +
                    "Para realizar a sua renovação e manter suas aulas ativas, fale com o nosso suporte por aqui mesmo.\n\n" +
                    "*(Caso já tenha realizado a renovação, desconsidere esta mensagem).*",
                    alunoNome, contratoId, alunoNivel, contratoDataFim, diasParaVencer
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
