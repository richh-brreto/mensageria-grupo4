package com.escola.mensageria.domain.entity;

public record Turma(Long id, String nome, String nivel, Integer limiteAlunos, String tipo, Long professorId) {

    public boolean possuiNome() {
        return nome != null && !nome.isBlank();
    }
}
