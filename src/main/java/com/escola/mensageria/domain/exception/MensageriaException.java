package com.escola.mensageria.domain.exception;

public abstract class MensageriaException extends RuntimeException {

    protected MensageriaException(String mensagem) {
        super(mensagem);
    }

    protected MensageriaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
