package com.escola.notification.adapters.usecase;

import com.escola.notification.domain.model.AulaNotificacaoEvent;
import com.escola.notification.domain.model.AulaNotificacaoPayload;
import com.escola.notification.domain.model.Participante;
import com.escola.notification.domain.ports.EmailServicePort;
import com.escola.notification.usecases.EnviarEmailNotificacaoUseCase;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnviarEmailNotificacaoUseCaseImpl implements EnviarEmailNotificacaoUseCase {

    private final EmailServicePort emailServicePort;

    public EnviarEmailNotificacaoUseCaseImpl(EmailServicePort emailServicePort) {
        this.emailServicePort = emailServicePort;
    }

    @Override
    public void processar(AulaNotificacaoEvent evento) {
        if (evento == null || evento.payload() == null) {
            return;
        }

        AulaNotificacaoPayload payload = evento.payload();
        List<Participante> destinatarios = new ArrayList<>();

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
