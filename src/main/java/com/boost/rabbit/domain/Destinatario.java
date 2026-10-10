package com.boost.rabbit.domain;

public class Destinatario {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private boolean ativo;

    // Getters e Setters
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}