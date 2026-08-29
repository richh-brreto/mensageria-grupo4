package com.escola.notification.usecases.impl;

import com.escola.notification.domain.model.LessonNotificationEvent;
import com.escola.notification.domain.model.LessonNotificationPayload;
import com.escola.notification.domain.model.Participant;
import com.escola.notification.domain.ports.EmailServicePort;
import com.escola.notification.usecases.SendLessonNotificationEmailUseCase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendLessonNotificationEmailUseCaseImpl implements SendLessonNotificationEmailUseCase {

    private final EmailServicePort emailServicePort;

    public SendLessonNotificationEmailUseCaseImpl(EmailServicePort emailServicePort) {
        this.emailServicePort = emailServicePort;
    }

    @Override
    public void processar(LessonNotificationEvent evento) {
        if (evento == null || evento.payload() == null) {
            return;
        }

        LessonNotificationPayload payload = evento.payload();
        List<Participant> destinatarios = new ArrayList<>();

        if (payload.professor() != null) {
            destinatarios.add(payload.professor());
        }
        if (payload.alunos() != null) {
            destinatarios.addAll(payload.alunos());
        }

        if (destinatarios.isEmpty()) {
            return;
        }

        String assunto = "Notificação de aula - " + payload.dataAula() + " | " + payload.horario();
        String corpo = "Olá!\n\n" +
                "Você está registrado na aula de código " + payload.aulaId() + ".\n" +
                "Data: " + payload.dataAula() + "\n" +
                "Horário: " + payload.horario() + "\n\n" +
                "Atenciosamente,\nEquipe de Mensageria Escolar";

        emailServicePort.enviarParaTodos(destinatarios, assunto, corpo);
    }
}
