package com.escola.notification.adapters.messaging;

import com.escola.notification.domain.entities.Notification;
import com.escola.notification.usecases.ports.input.SendEmailNotificationUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Consumer RabbitMQ que recebe mensagens de notificação e encaminha para o
 * Use Case de envio de e-mail. A desserialização e mapeamento devem ocorrer
 * no adapter para manter o usecase framework-free.
 */
@Component
public class RabbitMQConsumer {

    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    public RabbitMQConsumer(SendEmailNotificationUseCase sendEmailNotificationUseCase) {
        this.sendEmailNotificationUseCase = sendEmailNotificationUseCase;
    }

    @RabbitListener(queues = "${rabbitmq.queue.notification:notifications}")
    @Async("consumerExecutor")
    public void onMessage(Notification message) {
        // O framework (Jackson) mapeia o payload diretamente para Notification DTO.
        // Em cenários reais, criar um DTO e mapear para a entidade de domínio.
        sendEmailNotificationUseCase.send(message);
    }
}
