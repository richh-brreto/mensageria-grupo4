package com.escola.notification.adapters.scheduler;

import com.escola.notification.usecases.ProcessLessonNotificationsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LessonNotificationScheduler {

    private final ProcessLessonNotificationsUseCase processarNotificacoesAulaUseCase;

    public LessonNotificationScheduler(ProcessLessonNotificationsUseCase processarNotificacoesAulaUseCase) {
        this.processarNotificacoesAulaUseCase = processarNotificacoesAulaUseCase;
    }

    @Scheduled(fixedDelayString = "${app.notification.polling-ms:60000}")
    public void processar() {
        processarNotificacoesAulaUseCase.processar();
    }
}
