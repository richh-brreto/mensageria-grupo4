package com.escola.mensageria.usecase.impl;

import com.escola.mensageria.domain.entity.AvisoContrato;
import com.escola.mensageria.domain.gateway.WhatsAppServicePort;
import com.escola.mensageria.domain.support.Precondicoes;
import com.escola.mensageria.usecase.EnviarAvisosWhatsAppUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class EnviarAvisosWhatsAppUseCaseImpl implements EnviarAvisosWhatsAppUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnviarAvisosWhatsAppUseCaseImpl.class);

    private final WhatsAppServicePort whatsAppServicePort;

    public EnviarAvisosWhatsAppUseCaseImpl(WhatsAppServicePort whatsAppServicePort) {
        this.whatsAppServicePort = Precondicoes.naoNulo(whatsAppServicePort, "whatsAppServicePort");
    }

    @Override
    public Resultado executar(AvisoContrato aviso) {
        Precondicoes.naoNulo(aviso, "aviso");

        List<EnvioConfirmado> envios = new ArrayList<>();

        for (AvisoContrato.MensagemParaEnvio mensagem : aviso.mensagens()) {
            LOGGER.info("Enviando aviso de vencimento para {} ({})", mensagem.destinatario(), mensagem.telefone());

            WhatsAppServicePort.ResultadoEnvio resultado =
                    whatsAppServicePort.enviar(mensagem.telefone(), mensagem.texto());

            envios.add(new EnvioConfirmado(
                    mensagem.destinatario(),
                    mensagem.telefone(),
                    resultado.identificador()
            ));
        }

        LOGGER.info("Aviso de vencimento processado. {} mensagem(ns) enviada(s).", envios.size());

        return new Resultado(envios);
    }
}
