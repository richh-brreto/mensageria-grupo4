package com.escola.mensageria.domain.entity;

public record Professor(Long id, String nome, String email, String nivel, boolean ativo) {

    public boolean possuiNome() {
        return nome != null && !nome.isBlank();
    }
}
