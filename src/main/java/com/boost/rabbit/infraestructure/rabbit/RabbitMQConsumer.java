package com.boost.rabbit.infraestructure.rabbit;

import com.boost.rabbit.infraestructure.external.WhapiClient;
import com.boost.rabbit.infraestructure.processor.ContratoVencimentoMessageProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RabbitMQConsumer {

    private final ContratoVencimentoMessageProcessor processor;
    private final WhapiClient whapiClient;

    public RabbitMQConsumer(ContratoVencimentoMessageProcessor processor, WhapiClient whapiClient) {
        this.processor = processor;
        this.whapiClient = whapiClient;
    }

    @RabbitListener(queues = "contratos.vencimento.queue")
    public void consumirMensagem(String mensagem) throws IOException {
        String texto = processor.montarMensagem(mensagem);
        String telefone = processor.getTelefone(mensagem);
        whapiClient.enviarMensagem(telefone, texto);
    }
}