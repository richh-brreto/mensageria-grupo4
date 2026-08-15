package com.escola.notification.adapters.mail;

import com.escola.notification.domain.entities.Notification;
import com.escola.notification.usecases.ports.output.EmailSenderPort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Gateway de e-mail que utiliza Spring Mail (SMTP). Implementa o EmailSenderPort.
 * O envio é assíncrono para não bloquear o consumidor.
 */
@Component
public class SpringMailGateway implements EmailSenderPort {

    private final JavaMailSender mailSender;

    public SpringMailGateway(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async("emailExecutor")
    public void sendEmail(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getToEmail());
        message.setSubject(notification.getSubject());
        message.setText(notification.getBody());
        mailSender.send(message);
    }
}
