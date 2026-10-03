package com.escola.mensageria.domain.entity;

import com.escola.mensageria.domain.support.Precondicoes;
import com.escola.mensageria.domain.valueobject.Telefone;

public record Destinatario(Long id, String nome, String email, Telefone telefone, boolean ativo) {

    public Destinatario {
        nome = Precondicoes.textoObrigatorio(nome, "destinatario.nome");
        telefone = Precondicoes.naoNulo(telefone, "destinatario.telefone");
    }
}
