package com.boost.rabbit.infraestructure.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "contratos.vencimento.queue")
    public void consumirMensagem(String mensagem) {
        System.out.println("[x] Mensagem recebida da fila: " + mensagem);
    }
}