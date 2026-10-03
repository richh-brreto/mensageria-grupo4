package com.escola.mensageria.domain.entity;

import com.escola.mensageria.domain.support.Precondicoes;
import com.escola.mensageria.domain.valueobject.Telefone;

public record Aluno(Long id, String nome, String email, Telefone telefone, String nivel, boolean ativo) {

    public Aluno {
        nome = Precondicoes.textoObrigatorio(nome, "aluno.nome");
        telefone = Precondicoes.naoNulo(telefone, "aluno.telefone");
    }
}
