package com.escola.mensageria.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitMensageriaProperties.class)
public class RabbitMQConfig {

    @Bean
    public DirectExchange mensageriaExchange(RabbitMensageriaProperties properties) {
        return new DirectExchange(properties.getExchange(), true, false);
    }

    @Bean
    public Queue mensageriaFila(RabbitMensageriaProperties properties) {
        return QueueBuilder.durable(properties.getFila())
                .deadLetterExchange(properties.getDeadLetterExchange())
                .deadLetterRoutingKey(properties.getDeadLetterRoutingKey())
                .build();
    }

    @Bean
    public Binding mensageriaBinding(Queue mensageriaFila,
                                     DirectExchange mensageriaExchange,
                                     RabbitMensageriaProperties properties) {
        return BindingBuilder.bind(mensageriaFila)
                .to(mensageriaExchange)
                .with(properties.getRoutingKey());
    }

    @Bean
    public DirectExchange deadLetterExchange(RabbitMensageriaProperties properties) {
        return new DirectExchange(properties.getDeadLetterExchange(), true, false);
    }

    @Bean
    public Queue deadLetterFila(RabbitMensageriaProperties properties) {
        return QueueBuilder.durable(properties.getDeadLetterFila()).build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterFila,
                                     DirectExchange deadLetterExchange,
                                     RabbitMensageriaProperties properties) {
        return BindingBuilder.bind(deadLetterFila)
                .to(deadLetterExchange)
                .with(properties.getDeadLetterRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
