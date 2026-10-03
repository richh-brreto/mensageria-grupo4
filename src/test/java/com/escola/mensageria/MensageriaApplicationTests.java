package com.escola.mensageria;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.rabbitmq.listener.simple.auto-startup=false")
class MensageriaApplicationTests {

    @Test
    @DisplayName("o contexto sobe sem depender de RabbitMQ ou Twilio no startup")
    void contextoDeveSubir() {
    }
}
