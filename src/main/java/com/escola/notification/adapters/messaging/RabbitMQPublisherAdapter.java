//package com.escola.notification.adapters.messaging;
//
//import com.escola.notification.domain.entities.Notification;
//import com.escola.notification.usecases.ports.output.NotificationPublisherPort;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * Adapter que publica notificações no broker RabbitMQ.
// */
//@Component
//public class RabbitMQPublisherAdapter implements NotificationPublisherPort {
//
//    private final AmqpTemplate amqpTemplate;
//
//    public RabbitMQPublisherAdapter(AmqpTemplate amqpTemplate) {
//        this.amqpTemplate = amqpTemplate;
//    }
//
//    @Override
//    public void publish(Notification notification) {
//        String exchange = System.getProperty("rabbitmq.exchange.notification", "notifications-exchange");
//        String routingKey = System.getProperty("rabbitmq.routing.key", "notification.created");
//        amqpTemplate.convertAndSend(exchange, routingKey, notification);
//    }
//}
