package com.escola.notification.usecases.ports.input;

import com.escola.notification.domain.entities.Notification;

/**
 * Input Port responsável por enviar uma notificação por e-mail.
 * Deve ser projetado para execução assíncrona.
 */
public interface SendEmailNotificationUseCase {
    /**
     * Envia a notificação (assíncrono/adapter implementa a execução não bloqueante).
     */
    void send(Notification notification);
}
