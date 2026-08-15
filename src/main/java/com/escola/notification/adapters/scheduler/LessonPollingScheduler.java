package com.escola.notification.adapters.scheduler;

import com.escola.notification.usecases.ports.input.ProcessUpcomingNotificationsUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Scheduler que dispara periodicamente a varredura de aulas próximas.
 * Usa o Input Port ProcessUpcomingNotificationsUseCase.
 */
@Component
public class LessonPollingScheduler {

    private final ProcessUpcomingNotificationsUseCase processUpcomingNotificationsUseCase;

    public LessonPollingScheduler(ProcessUpcomingNotificationsUseCase processUpcomingNotificationsUseCase) {
        this.processUpcomingNotificationsUseCase = processUpcomingNotificationsUseCase;
    }

    /**
     * Executa a varredura a cada minuto (exemplo). A execução é delegada
     * de forma assíncrona para evitar bloqueio do agendador.
     */
    @Scheduled(fixedDelayString = "${scheduling.polling.fixedDelayMs:60000}")
    @Async("schedulerExecutor")
    public void pollLessons() {
        processUpcomingNotificationsUseCase.processUpcoming();
    }
}
