package com.escola.mensageria.adapters.inbound.dto;

import com.escola.mensageria.domain.entity.AvisoContrato;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvisoContratoRequestTest {

    private static AvisoContratoRequest request() {
        return new AvisoContratoRequest(
                new AvisoContratoRequest.ContratoRequest(12L, "Grupo",
                        LocalDate.of(2026, 8, 1), LocalDate.of(2026, 10, 20)),
                new AvisoContratoRequest.AlunoRequest(1L, "Guilherme Silva2", "guilherme.silva@sptech.school",
                        "11977776666", "Intermediário", true),
                null,
                new AvisoContratoRequest.TurmaRequest(1L, "ADS-2B", "Intermediário", 30, "Grupo", 1L),
                new AvisoContratoRequest.DestinatarioRequest(1L, "Administrador Sistema", "admin@sptech.school",
                        "1199999888", true),
                18,
                LocalDate.of(2026, 10, 2),
                LocalDateTime.of(2026, 10, 2, 10, 15, 30)
        );
    }

    @Test
    @DisplayName("converte o payload da fila na entidade de dominio")
    void deveConverterPayloadParaDominio() {
        AvisoContrato aviso = request().paraDominio();

        assertEquals(12L, aviso.contrato().id());
        assertEquals(LocalDate.of(2026, 10, 20), aviso.contrato().dataFim());

        assertEquals("Guilherme Silva2", aviso.aluno().nome());
        assertEquals("Intermediário", aviso.aluno().nivel());
        assertTrue(aviso.aluno().ativo());
        assertEquals("+5511977776666", aviso.aluno().telefone().getValor());

        assertEquals("ADS-2B", aviso.turma().nome());
        assertEquals(1L, aviso.turma().professorId());
        assertEquals("Intermediário", aviso.turma().nivel());

        assertEquals("Administrador Sistema", aviso.destinatario().nome());
        assertEquals("+551199999888", aviso.destinatario().telefone().getValor());

        assertEquals(18, aviso.diasParaVencer());
        assertEquals(LocalDate.of(2026, 10, 2), aviso.dataAviso());
        assertEquals(LocalDateTime.of(2026, 10, 2, 10, 15, 30), aviso.publicadoEm());
    }

    @Test
    @DisplayName("mantem o professor nulo quando o payload nao o traz")
    void deveAceitarProfessorNulo() {
        assertNull(request().paraDominio().professor());
    }

    @Test
    @DisplayName("converte o professor quando ele vem preenchido")
    void deveConverterProfessorPreenchido() {
        AvisoContratoRequest comProfessor = new AvisoContratoRequest(
                request().contrato(), request().aluno(),
                new AvisoContratoRequest.ProfessorRequest(1L, "Ricardo", "ricardo@sptech.school",
                        "Intermediário", true),
                request().turma(), request().destinatario(), 18,
                LocalDate.of(2026, 10, 2), LocalDateTime.of(2026, 10, 2, 10, 15, 30));

        assertEquals("Ricardo", comProfessor.paraDominio().professor().nome());
    }
}
