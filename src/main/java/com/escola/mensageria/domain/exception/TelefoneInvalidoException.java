package com.escola.mensageria.domain.exception;

public class TelefoneInvalidoException extends MensageriaException {

    private final transient String numeroRecebido;

    public TelefoneInvalidoException(String numeroRecebido, String motivo) {
        super("Telefone invalido [" + numeroRecebido + "]: " + motivo);
        this.numeroRecebido = numeroRecebido;
    }

    public String getNumeroRecebido() {
        return numeroRecebido;
    }
}
