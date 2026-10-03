package com.escola.mensageria.domain.gateway;

import com.escola.mensageria.domain.exception.FalhaEnvioWhatsAppException;
import com.escola.mensageria.domain.support.Precondicoes;
import com.escola.mensageria.domain.valueobject.Telefone;

public interface WhatsAppServicePort {

    ResultadoEnvio enviar(Telefone destino, String mensagem) throws FalhaEnvioWhatsAppException;

    record ResultadoEnvio(String identificador, Situacao situacao) {

        public ResultadoEnvio {
            identificador = Precondicoes.textoObrigatorio(identificador, "identificador");
            situacao = Precondicoes.naoNulo(situacao, "situacao");
        }

        public static ResultadoEnvio enviado(String identificador) {
            return new ResultadoEnvio(identificador, Situacao.ENVIADA);
        }
    }

    enum Situacao {
        ENVIADA,
        FALHOU
    }
}
