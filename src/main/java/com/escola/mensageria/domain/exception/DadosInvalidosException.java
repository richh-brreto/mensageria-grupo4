package com.escola.mensageria.domain.exception;

public class DadosInvalidosException extends MensageriaException {

    private final transient String campo;

    public DadosInvalidosException(String campo, String motivo) {
        super("Dado invalido em '" + campo + "': " + motivo);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
