package com.escola.notification.usecases.ports.output;

import com.escola.notification.domain.entities.Notification;

/**
 * Output Port para publicar notificações em um broker (ex: RabbitMQ).
 * Deve ser assíncrono e resiliente.
 */
public interface NotificationPublisherPort {
    /**
     * Publica a notificação de forma assíncrona.
     */
    void publish(Notification notification);
}
