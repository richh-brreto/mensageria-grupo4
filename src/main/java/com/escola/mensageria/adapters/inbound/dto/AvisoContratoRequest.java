package com.escola.mensageria.adapters.inbound.dto;

import com.escola.mensageria.domain.entity.Aluno;
import com.escola.mensageria.domain.entity.AvisoContrato;
import com.escola.mensageria.domain.entity.Contrato;
import com.escola.mensageria.domain.entity.Destinatario;
import com.escola.mensageria.domain.entity.Professor;
import com.escola.mensageria.domain.entity.Turma;
import com.escola.mensageria.domain.valueobject.Telefone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AvisoContratoRequest(
        @JsonProperty("contrato") ContratoRequest contrato,
        @JsonProperty("aluno") AlunoRequest aluno,
        @JsonProperty("professor") ProfessorRequest professor,
        @JsonProperty("turma") TurmaRequest turma,
        @JsonProperty("destinatario") DestinatarioRequest destinatario,
        @JsonProperty("diasParaVencer") Integer diasParaVencer,
        @JsonProperty("dataAviso") LocalDate dataAviso,
        @JsonProperty("publicadoEm") LocalDateTime publicadoEm
) {

    public AvisoContrato paraDominio() {
        return new AvisoContrato(
                contrato == null ? null : contrato.paraDominio(),
                aluno == null ? null : aluno.paraDominio(),
                professor == null ? null : professor.paraDominio(),
                turma == null ? null : turma.paraDominio(),
                destinatario == null ? null : destinatario.paraDominio(),
                diasParaVencer,
                dataAviso,
                publicadoEm
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContratoRequest(
            @JsonProperty("id") Long id,
            @JsonProperty("tipo") String tipo,
            @JsonProperty("dataInicio") LocalDate dataInicio,
            @JsonProperty("dataFim") LocalDate dataFim
    ) {

        public Contrato paraDominio() {
            return new Contrato(id, tipo, dataInicio, dataFim);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AlunoRequest(
            @JsonProperty("id") Long id,
            @JsonProperty("nome") String nome,
            @JsonProperty("email") String email,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("nível") String nivel,
            @JsonProperty("ativo") Boolean ativo
    ) {

        public Aluno paraDominio() {
            return new Aluno(
                    id,
                    nome,
                    email,
                    telefone == null ? null : Telefone.de(telefone),
                    nivel,
                    Boolean.TRUE.equals(ativo)
            );
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfessorRequest(
            @JsonProperty("id") Long id,
            @JsonProperty("nome") String nome,
            @JsonProperty("email") String email,
            @JsonProperty("nível") String nivel,
            @JsonProperty("ativo") Boolean ativo
    ) {

        public Professor paraDominio() {
            return new Professor(id, nome, email, nivel, Boolean.TRUE.equals(ativo));
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TurmaRequest(
            @JsonProperty("id") Long id,
            @JsonProperty("nome") String nome,
            @JsonProperty("nível") String nivel,
            @JsonProperty("limiteAlunos") Integer limiteAlunos,
            @JsonProperty("tipo") String tipo,
            @JsonProperty("professorId") Long professorId
    ) {

        public Turma paraDominio() {
            return new Turma(id, nome, nivel, limiteAlunos, tipo, professorId);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record DestinatarioRequest(
            @JsonProperty("id") Long id,
            @JsonProperty("nome") String nome,
            @JsonProperty("email") String email,
            @JsonProperty("telefone") String telefone,
            @JsonProperty("ativo") Boolean ativo
    ) {

        public Destinatario paraDominio() {
            return new Destinatario(
                    id,
                    nome,
                    email,
                    telefone == null ? null : Telefone.de(telefone),
                    Boolean.TRUE.equals(ativo)
            );
        }
    }
}
