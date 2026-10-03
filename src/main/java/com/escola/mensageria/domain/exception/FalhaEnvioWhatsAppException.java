package com.escola.mensageria.domain.exception;

public class FalhaEnvioWhatsAppException extends MensageriaException {

    public FalhaEnvioWhatsAppException(String mensagem) {
        super(mensagem);
    }

    public FalhaEnvioWhatsAppException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
