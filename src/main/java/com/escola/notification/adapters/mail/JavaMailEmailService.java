package com.escola.notification.adapters.mail;

import com.escola.notification.domain.model.Participante;
import com.escola.notification.domain.ports.EmailServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JavaMailEmailService implements EmailServicePort {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaMailEmailService.class);

    private final JavaMailSender javaMailSender;

    public JavaMailEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void enviar(String destinatario, String assunto, String corpo) {
        if (destinatario == null || destinatario.isBlank()) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(corpo);
            javaMailSender.send(message);
        } catch (MailException exception) {
            LOGGER.warn("Não foi possível enviar e-mail para {}. Motivo: {}", destinatario, exception.getMessage());
        }
    }

    @Override
    public void enviarParaTodos(List<Participante> destinatarios, String assunto, String corpo) {
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
