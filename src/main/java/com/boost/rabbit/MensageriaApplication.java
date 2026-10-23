package com.boost.rabbit;

import com.boost.rabbit.infraestructure.external.WhapiClient;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableRabbit
public class MensageriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MensageriaApplication.class, args);
    }

    @Bean
    CommandLineRunner enviarMensagemAoIniciar(WhapiClient whapiClient) {
        return args -> {
        };
    }
}
