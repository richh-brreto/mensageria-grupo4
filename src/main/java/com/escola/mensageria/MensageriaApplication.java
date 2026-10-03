package com.escola.mensageria;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MensageriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MensageriaApplication.class, args);
    }
}
