package com.escola.notification.adapters.usecase;

import com.escola.notification.domain.model.AulaNotificacaoEvent;
import com.escola.notification.domain.model.AulaNotificacaoPayload;
import com.escola.notification.domain.ports.AulaRepositoryPort;
import com.escola.notification.domain.ports.EventPublisherPort;
import com.escola.notification.usecases.ProcessarNotificacoesAulaUseCase;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ProcessarNotificacoesAulaUseCaseImpl implements ProcessarNotificacoesAulaUseCase {

    private final AulaRepositoryPort aulaRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    public ProcessarNotificacoesAulaUseCaseImpl(AulaRepositoryPort aulaRepositoryPort, EventPublisherPort eventPublisherPort) {
        this.aulaRepositoryPort = aulaRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    @Async("schedulerExecutor")
    public void processar() {
        for (AulaNotificacaoPayload aula : aulaRepositoryPort.buscarAulasParaNotificacao()) {
            var evento = new AulaNotificacaoEvent(
                    UUID.randomUUID(),
                    AulaNotificacaoEvent.EVENT_TYPE,
                    Instant.now(),
                    aula
            );
            eventPublisherPort.publicar(evento);
        }
    }
}
