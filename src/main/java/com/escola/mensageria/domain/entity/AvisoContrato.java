package com.escola.mensageria.domain.entity;

import com.escola.mensageria.domain.support.Precondicoes;
import com.escola.mensageria.domain.valueobject.Telefone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record AvisoContrato(
        Contrato contrato,
        Aluno aluno,
        Professor professor,
        Turma turma,
        Destinatario destinatario,
        Integer diasParaVencer,
        LocalDate dataAviso,
        LocalDateTime publicadoEm
) {

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String ASSINATURA = "Equipe Boost";

    public AvisoContrato {
        contrato = Precondicoes.naoNulo(contrato, "contrato");
        aluno = Precondicoes.naoNulo(aluno, "aluno");
        destinatario = Precondicoes.naoNulo(destinatario, "destinatario");
    }

    public List<MensagemParaEnvio> mensagens() {
        return List.of(
                new MensagemParaEnvio(destinatario.nome(), destinatario.telefone(), mensagemParaDestinatario()),
                new MensagemParaEnvio(aluno.nome(), aluno.telefone(), mensagemParaAluno())
        );
    }

    public String mensagemParaDestinatario() {
        StringBuilder texto = new StringBuilder()
                .append("Olá, ").append(destinatario.nome()).append("!")
                .append("\n\n")
                .append(referenciaContrato())
                .append("do aluno ").append(aluno.nome()).append(' ').append(prazo()).append('.');

        if (turma != null && turma.possuiNome()) {
            acrescentarLinha(texto, "Turma: " + turma.nome() + ".");
        }
        if (professor != null && professor.possuiNome()) {
            acrescentarLinha(texto, "Professor responsável: " + professor.nome() + ".");
        }

        return texto
                .append("\n\n")
                .append("Solicitamos a regularização da renovação antes da data de vencimento.")
                .append("\n\n")
                .append("Atenciosamente,")
                .append('\n')
                .append(ASSINATURA)
                .toString();
    }

    public String mensagemParaAluno() {
        StringBuilder texto = new StringBuilder()
                .append("Olá, ").append(aluno.nome()).append("!")
                .append("\n\n")
                .append("Informamos que o seu contrato ");

        if (turma != null && turma.possuiNome()) {
            texto.append("da turma ").append(turma.nome()).append(' ');
        }

        return texto
                .append(prazo()).append('.')
                .append("\n\n")
                .append("Renove sua matrícula para não perder sua vaga.")
                .append("\n\n")
                .append("Atenciosamente,")
                .append('\n')
                .append(ASSINATURA)
                .toString();
    }

    private String referenciaContrato() {
        return contrato.id() == null ? "O contrato " : "O contrato nº " + contrato.id() + " ";
    }

    private String prazo() {
        String vencimento = contrato.dataFim() == null
                ? "data a definir"
                : DATA_BR.format(contrato.dataFim());

        if (diasParaVencer == null || diasParaVencer <= 0) {
            return "vence em " + vencimento;
        }

        String restante = diasParaVencer == 1
                ? "falta 1 dia"
                : "faltam " + diasParaVencer + " dias";

        return "vence em " + vencimento + " (" + restante + ")";
    }

    private static void acrescentarLinha(StringBuilder texto, String linha) {
        if (linha != null && !linha.isBlank()) {
            texto.append('\n').append(linha);
        }
    }

    public record MensagemParaEnvio(String destinatario, Telefone telefone, String texto) {
    }
}
