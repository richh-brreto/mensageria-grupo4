package com.escola.mensageria.domain.support;

import com.escola.mensageria.domain.exception.DadosInvalidosException;

public final class Precondicoes {

    private Precondicoes() {
    }

    public static String textoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DadosInvalidosException(campo, "nao pode ser nulo ou vazio.");
        }
        return valor.trim();
    }

    public static <T> T naoNulo(T valor, String campo) {
        if (valor == null) {
            throw new DadosInvalidosException(campo, "nao pode ser nulo.");
        }
        return valor;
    }
}
