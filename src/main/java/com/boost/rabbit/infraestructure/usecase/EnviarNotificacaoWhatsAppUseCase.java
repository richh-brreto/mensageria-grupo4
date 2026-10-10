package com.boost.rabbit.infraestructure.usecase;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EnviarNotificacaoWhatsAppUseCase {

    public void executar(String telefoneDestinatario) {
        if (telefoneDestinatario == null || telefoneDestinatario.isEmpty()) {
            return;
        }

        // Limpa e formata o número
        String numeroLimpo = telefoneDestinatario.replaceAll("[^0-9]", "");
        if (!numeroLimpo.startsWith("55") && numeroLimpo.length() <= 11) {
            numeroLimpo = "55" + numeroLimpo;
        }

        String destinatarioTwilio = "whatsapp:+" + numeroLimpo;
        String remetenteTwilio = "whatsapp:+17372508034";

        System.out.println("[->] Executando Caso de Uso -> Enviando para: " + destinatarioTwilio);

        // Disparo do Twilio
        Message message = Message.creator(
                        new PhoneNumber(destinatarioTwilio),
                        new PhoneNumber(remetenteTwilio),
                        (String) null
                )
                .setContentSid("HXfe5ab5f00277942d4d4200328b4d403c")
                .setContentVariables(new JSONObject(new HashMap<String, Object>() {{
                    put("1", "Sua consulta/aviso");
                }}).toString())
                .create();

        System.out.println("[✓] Mensagem enviada com sucesso! SID: " + message.getSid());
    }
}