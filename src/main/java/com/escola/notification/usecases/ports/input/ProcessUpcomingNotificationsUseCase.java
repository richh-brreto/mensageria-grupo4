package com.escola.notification.usecases.ports.input;

/**
 * Porta de entrada (Input Port) para iniciar o processamento assíncrono
 * das próximas aulas que irão gerar notificações.
 * Implementações devem executar de forma assíncrona e não bloquear o chamador.
 */
public interface ProcessUpcomingNotificationsUseCase {
    /**
     * Inicia o processo de varredura e publicação de notificações.
     */
    void processUpcoming();
}
