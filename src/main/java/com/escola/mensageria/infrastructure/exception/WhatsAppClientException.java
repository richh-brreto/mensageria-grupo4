package com.escola.mensageria.infrastructure.exception;

public class WhatsAppClientException extends RuntimeException {

    public WhatsAppClientException(String mensagem) {
        super(mensagem);
    }

    public WhatsAppClientException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
