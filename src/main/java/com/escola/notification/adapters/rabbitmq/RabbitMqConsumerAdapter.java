package com.escola.notification.adapters.rabbitmq;

import com.escola.notification.domain.model.LessonNotificationEvent;
import com.escola.notification.usecases.SendLessonNotificationEmailUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConsumerAdapter {

    private final SendLessonNotificationEmailUseCase enviarEmailNotificacaoUseCase;

    public RabbitMqConsumerAdapter(SendLessonNotificationEmailUseCase enviarEmailNotificacaoUseCase) {
        this.enviarEmailNotificacaoUseCase = enviarEmailNotificacaoUseCase;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.name}")
    public void consumir(LessonNotificationEvent evento) {
        enviarEmailNotificacaoUseCase.processar(evento);
    }
}
