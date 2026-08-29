package com.escola.notification.usecases.impl;

import com.escola.notification.domain.model.LessonNotificationEvent;
import com.escola.notification.domain.model.LessonNotificationPayload;
import com.escola.notification.domain.ports.LessonNotificationRepositoryPort;
import com.escola.notification.domain.ports.EventPublisherPort;
import com.escola.notification.usecases.ProcessLessonNotificationsUseCase;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ProcessLessonNotificationsUseCaseImpl implements ProcessLessonNotificationsUseCase {

    private final LessonNotificationRepositoryPort aulaRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    public ProcessLessonNotificationsUseCaseImpl(LessonNotificationRepositoryPort aulaRepositoryPort, EventPublisherPort eventPublisherPort) {
        this.aulaRepositoryPort = aulaRepositoryPort;
        this.eventPublisherPort = eventPublisherPort;
    }

    @Override
    @Async("schedulerExecutor")
    public void processar() {
        for (LessonNotificationPayload aula : aulaRepositoryPort.buscarAulasParaNotificacao()) {
            var evento = new LessonNotificationEvent(
                    UUID.randomUUID(),
                    LessonNotificationEvent.EVENT_TYPE,
                    Instant.now(),
                    aula
            );
            eventPublisherPort.publicar(evento);
        }
    }
}
