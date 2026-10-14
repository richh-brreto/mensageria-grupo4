package com.boost.rabbit.domain;

public class Notificacao {
    private Destinatario destinatario;
    private int diasParaVencer;

    public Destinatario getDestinatario() { return destinatario; }
    public void setDestinatario(Destinatario destinatario) { this.destinatario = destinatario; }
}