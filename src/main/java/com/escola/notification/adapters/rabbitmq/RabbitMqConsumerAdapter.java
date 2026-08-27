package com.escola.notification.adapters.rabbitmq;

import com.escola.notification.domain.model.AulaNotificacaoEvent;
import com.escola.notification.usecases.EnviarEmailNotificacaoUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConsumerAdapter {

    private final EnviarEmailNotificacaoUseCase enviarEmailNotificacaoUseCase;

    public RabbitMqConsumerAdapter(EnviarEmailNotificacaoUseCase enviarEmailNotificacaoUseCase) {
        this.enviarEmailNotificacaoUseCase = enviarEmailNotificacaoUseCase;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.name}")
    public void consumir(AulaNotificacaoEvent evento) {
        enviarEmailNotificacaoUseCase.processar(evento);
    }
}
