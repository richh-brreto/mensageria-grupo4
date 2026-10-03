package com.escola.mensageria.domain.entity;

import com.escola.mensageria.domain.exception.DadosInvalidosException;
import com.escola.mensageria.domain.valueobject.Telefone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvisoContratoTest {

    private static final Contrato CONTRATO = new Contrato(12L, "Grupo",
            LocalDate.of(2026, 8, 1), LocalDate.of(2026, 10, 20));

    private static final Aluno ALUNO = new Aluno(1L, "Guilherme Silva2",
            "guilherme.silva@sptech.school", Telefone.de("11977776666"), "Intermediário", true);

    private static final Turma TURMA = new Turma(1L, "ADS-2B", "Intermediário", 30, "Grupo", 1L);

    private static final Destinatario ADMIN = new Destinatario(1L, "Administrador Sistema",
            "admin@sptech.school", Telefone.de("1199999888"), true);

    private static final LocalDateTime PUBLICADO_EM = LocalDateTime.of(2026, 10, 2, 10, 15, 30);

    @Test
    @DisplayName("gera uma mensagem para o destinatario e uma para o aluno")
    void deveGerarDuasMensagens() {
        AvisoContrato aviso = aviso(new Professor(1L, "Ricardo", "ricardo@sptech.school", "Intermediário", true));

        List<AvisoContrato.MensagemParaEnvio> mensagens = aviso.mensagens();

        assertEquals(2, mensagens.size());
        assertEquals("Administrador Sistema", mensagens.get(0).destinatario());
        assertEquals("+551199999888", mensagens.get(0).telefone().getValor());
        assertEquals("Guilherme Silva2", mensagens.get(1).destinatario());
        assertEquals("+5511977776666", mensagens.get(1).telefone().getValor());
    }

    @Test
    @DisplayName("a mensagem do destinatario traz contrato, prazo, turma e professor")
    void deveMontarMensagemDoDestinatario() {
        AvisoContrato aviso = aviso(new Professor(1L, "Ricardo", "ricardo@sptech.school", "Intermediário", true));

        String texto = aviso.mensagemParaDestinatario();

        assertTrue(texto.contains("Olá, Administrador Sistema!"));
        assertTrue(texto.contains("contrato nº 12"));
        assertTrue(texto.contains("Guilherme Silva2"));
        assertTrue(texto.contains("vence em 20/10/2026 (faltam 18 dias)"));
        assertTrue(texto.contains("Turma: ADS-2B."));
        assertTrue(texto.contains("Professor responsável: Ricardo."));
        assertTrue(texto.contains("Equipe Boost"));
    }

    @Test
    @DisplayName("a mensagem do aluno convida a renovar a matricula")
    void deveMontarMensagemDoAluno() {
        String texto = aviso(null).mensagemParaAluno();

        assertTrue(texto.contains("Olá, Guilherme Silva2!"));
        assertTrue(texto.contains("da turma ADS-2B"));
        assertTrue(texto.contains("vence em 20/10/2026 (faltam 18 dias)"));
        assertTrue(texto.contains("Renove sua matrícula"));
    }

    @Test
    @DisplayName("aceita professor nulo sem quebrar as mensagens")
    void deveAceitarProfessorNulo() {
        AvisoContrato aviso = aviso(null);

        assertFalse(aviso.mensagemParaDestinatario().contains("Professor responsável"));
        assertTrue(aviso.mensagemParaDestinatario().contains("Turma: ADS-2B."));
    }

    @Test
    @DisplayName("aceita turma nula sem quebrar as mensagens")
    void deveAceitarTurmaNula() {
        AvisoContrato aviso = new AvisoContrato(CONTRATO, ALUNO, null, null, ADMIN, 18,
                LocalDate.of(2026, 10, 2), PUBLICADO_EM);

        assertFalse(aviso.mensagemParaAluno().contains("turma"));
        assertTrue(aviso.mensagemParaAluno().contains("seu contrato vence"));
    }

    @Test
    @DisplayName("usa o singular quando falta um unico dia")
    void deveUsarSingularParaUmDia() {
        AvisoContrato aviso = new AvisoContrato(CONTRATO, ALUNO, null, TURMA, ADMIN, 1,
                LocalDate.of(2026, 10, 2), PUBLICADO_EM);

        assertTrue(aviso.mensagemParaAluno().contains("(falta 1 dia)"));
    }

    @Test
    @DisplayName("omite a contagem de dias quando ela nao vem no payload")
    void deveOmitirContagemDeDiasAusente() {
        AvisoContrato aviso = new AvisoContrato(CONTRATO, ALUNO, null, TURMA, ADMIN, null,
                LocalDate.of(2026, 10, 2), PUBLICADO_EM);

        assertTrue(aviso.mensagemParaAluno().contains("vence em 20/10/2026"));
        assertFalse(aviso.mensagemParaAluno().contains("faltam"));
    }

    @Test
    @DisplayName("exige contrato, aluno e destinatario")
    void deveExigirCamposEssenciais() {
        assertThrows(DadosInvalidosException.class, () -> new AvisoContrato(
                null, ALUNO, null, TURMA, ADMIN, 18, null, PUBLICADO_EM));
        assertThrows(DadosInvalidosException.class, () -> new AvisoContrato(
                CONTRATO, null, null, TURMA, ADMIN, 18, null, PUBLICADO_EM));
        assertThrows(DadosInvalidosException.class, () -> new AvisoContrato(
                CONTRATO, ALUNO, null, TURMA, null, 18, null, PUBLICADO_EM));
    }

    private static AvisoContrato aviso(Professor professor) {
        return new AvisoContrato(CONTRATO, ALUNO, professor, TURMA, ADMIN, 18,
                LocalDate.of(2026, 10, 2), PUBLICADO_EM);
    }
}
