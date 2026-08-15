package com.escola.notification.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração estrutural do RabbitMQ: fila, exchange e binding.
 * Valores podem ser parametrizados via application.properties.
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue notificationQueue() {
        return new Queue(System.getProperty("rabbitmq.queue.notification", "notifications"), true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(System.getProperty("rabbitmq.exchange.notification", "notifications-exchange"));
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange notificationExchange) {
        String routingKey = System.getProperty("rabbitmq.routing.key", "notification.#");
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(routingKey);
    }
}
