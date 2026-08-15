package com.escola.notification.adapters.usecase;

import com.escola.notification.domain.entities.Lesson;
import com.escola.notification.domain.entities.Notification;
import com.escola.notification.usecases.ports.input.ProcessUpcomingNotificationsUseCase;
import com.escola.notification.usecases.ports.output.LessonRepositoryPort;
import com.escola.notification.usecases.ports.output.NotificationPublisherPort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Implementação do Use Case (Input Port) que processa próximas aulas.
 * Permanece na camada de Adapter/UseCase para orquestração entre ports.
 */
@Service
public class ProcessUpcomingNotificationsUseCaseImpl implements ProcessUpcomingNotificationsUseCase {

    private final LessonRepositoryPort lessonRepository;
    private final NotificationPublisherPort notificationPublisher;

    public ProcessUpcomingNotificationsUseCaseImpl(LessonRepositoryPort lessonRepository, NotificationPublisherPort notificationPublisher) {
        this.lessonRepository = lessonRepository;
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    @Async("schedulerExecutor")
    public void processUpcoming() {
        // Janela de exemplo: agora até +30 minutos
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime windowEnd = now.plusMinutes(30);

        List<Lesson> lessons = lessonRepository.findLessonsBetween(now, windowEnd);

        // Orquestração leve: para cada aula, cria uma Notification e publica
        for (Lesson l : lessons) {
            Notification notification = new Notification(
                    l.getId(),
                    l.getTeacherId(),
                    "teacher@example.com", // placeholder - adapter deve mapear
                    "Aula próxima",
                    "Sua aula está chegando.",
                    l.getScheduledAt()
            );
            notificationPublisher.publish(notification);
        }
    }
}
