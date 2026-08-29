package com.escola.notification.adapters.rabbitmq;

import com.escola.notification.domain.model.LessonNotificationEvent;
import com.escola.notification.domain.ports.EventPublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqEventPublisherAdapter implements EventPublisherPort {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public RabbitMqEventPublisherAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(LessonNotificationEvent evento) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, evento);
    }
}
