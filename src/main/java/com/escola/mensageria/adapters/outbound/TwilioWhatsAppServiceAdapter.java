package com.escola.mensageria.adapters.outbound;

import com.escola.mensageria.domain.exception.FalhaEnvioWhatsAppException;
import com.escola.mensageria.domain.gateway.WhatsAppServicePort;
import com.escola.mensageria.domain.support.Precondicoes;
import com.escola.mensageria.domain.valueobject.Telefone;
import com.escola.mensageria.infrastructure.client.TwilioWhatsAppClient;
import com.escola.mensageria.infrastructure.exception.WhatsAppClientException;
import org.springframework.stereotype.Component;

@Component
public class TwilioWhatsAppServiceAdapter implements WhatsAppServicePort {

    private final TwilioWhatsAppClient twilioWhatsAppClient;

    public TwilioWhatsAppServiceAdapter(TwilioWhatsAppClient twilioWhatsAppClient) {
        this.twilioWhatsAppClient = twilioWhatsAppClient;
    }

    @Override
    public ResultadoEnvio enviar(Telefone destino, String mensagem) {
        Precondicoes.naoNulo(destino, "destino");
        String corpo = Precondicoes.textoObrigatorio(mensagem, "mensagem");

        try {
            TwilioWhatsAppClient.EnvioBruto envio =
                    twilioWhatsAppClient.enviar(destino.comPrefixoWhatsApp(), corpo);

            return ResultadoEnvio.enviado(envio.sid());
        } catch (WhatsAppClientException excecao) {
            throw new FalhaEnvioWhatsAppException(
                    "Nao foi possivel enviar a mensagem de WhatsApp para " + destino.getValor() + ".",
                    excecao
            );
        }
    }
}
