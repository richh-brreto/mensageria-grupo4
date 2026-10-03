package com.escola.mensageria.adapters.inbound;

import com.escola.mensageria.adapters.inbound.dto.AvisoContratoRequest;
import com.escola.mensageria.domain.entity.AvisoContrato;
import com.escola.mensageria.domain.exception.MensageriaException;
import com.escola.mensageria.usecase.EnviarAvisosWhatsAppUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AvisoContratoRabbitListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvisoContratoRabbitListener.class);

    private final EnviarAvisosWhatsAppUseCase enviarAvisosWhatsAppUseCase;

    public AvisoContratoRabbitListener(EnviarAvisosWhatsAppUseCase enviarAvisosWhatsAppUseCase) {
        this.enviarAvisosWhatsAppUseCase = enviarAvisosWhatsAppUseCase;
    }

    @RabbitListener(queues = "${app.rabbitmq.fila}")
    public void aoReceberAviso(AvisoContratoRequest request) {
        try {
            AvisoContrato aviso = request.paraDominio();

            EnviarAvisosWhatsAppUseCase.Resultado resultado = enviarAvisosWhatsAppUseCase.executar(aviso);

            resultado.envios().forEach(envio -> LOGGER.info(
                    "Aviso enviado para {} ({}). Identificador: {}",
                    envio.destinatario(),
                    envio.telefone(),
                    envio.identificadorMensagem()
            ));
        } catch (MensageriaException excecao) {
            LOGGER.error("Falha ao processar o aviso de vencimento. A mensagem sera enviada para a fila de erros.", excecao);
            throw excecao;
        }
    }
}
