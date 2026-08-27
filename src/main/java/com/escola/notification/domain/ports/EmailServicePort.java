package com.escola.notification.domain.ports;

import com.escola.notification.domain.model.Participante;

import java.util.List;

public interface EmailServicePort {
    void enviar(String destinatario, String assunto, String corpo);

    default void enviarParaTodos(List<Participante> destinatarios, String assunto, String corpo) {
        if (destinatarios == null || destinatarios.isEmpty()) {
            return;
        }

        for (Participante destinatario : destinatarios) {
            if (destinatario != null && destinatario.email() != null && !destinatario.email().isBlank()) {
                enviar(destinatario.email(), assunto, corpo);
            }
        }
    }
}
