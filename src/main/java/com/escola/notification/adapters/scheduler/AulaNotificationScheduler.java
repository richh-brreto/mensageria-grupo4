package com.escola.notification.adapters.scheduler;

import com.escola.notification.usecases.ProcessarNotificacoesAulaUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AulaNotificationScheduler {

    private final ProcessarNotificacoesAulaUseCase processarNotificacoesAulaUseCase;

    public AulaNotificationScheduler(ProcessarNotificacoesAulaUseCase processarNotificacoesAulaUseCase) {
        this.processarNotificacoesAulaUseCase = processarNotificacoesAulaUseCase;
    }

    @Scheduled(fixedDelayString = "${app.notification.polling-ms:60000}")
    public void processar() {
        processarNotificacoesAulaUseCase.processar();
    }
}
