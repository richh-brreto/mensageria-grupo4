package com.escola.mensageria.usecase.impl;

import com.escola.mensageria.domain.entity.Aluno;
import com.escola.mensageria.domain.entity.AvisoContrato;
import com.escola.mensageria.domain.entity.Contrato;
import com.escola.mensageria.domain.entity.Destinatario;
import com.escola.mensageria.domain.entity.Turma;
import com.escola.mensageria.domain.exception.FalhaEnvioWhatsAppException;
import com.escola.mensageria.domain.valueobject.Telefone;
import com.escola.mensageria.usecase.EnviarAvisosWhatsAppUseCase;
import com.escola.mensageria.usecase.WhatsAppServicePortSpy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnviarAvisosWhatsAppUseCaseImplTest {

    private static final AvisoContrato AVISO = new AvisoContrato(
            new Contrato(12L, "Grupo", LocalDate.of(2026, 8, 1), LocalDate.of(2026, 10, 20)),
            new Aluno(1L, "Guilherme Silva2", "guilherme.silva@sptech.school",
                    Telefone.de("11977776666"), "Intermediário", true),
            null,
            new Turma(1L, "ADS-2B", "Intermediário", 30, "Grupo", 1L),
            new Destinatario(1L, "Administrador Sistema", "admin@sptech.school",
                    Telefone.de("1199999888"), true),
            18,
            LocalDate.of(2026, 10, 2),
            LocalDateTime.of(2026, 10, 2, 10, 15, 30)
    );

    private WhatsAppServicePortSpy porta;
    private EnviarAvisosWhatsAppUseCase useCase;

    @BeforeEach
    void setUp() {
        porta = new WhatsAppServicePortSpy();
        useCase = new EnviarAvisosWhatsAppUseCaseImpl(porta);
    }

    @Test
    @DisplayName("envia uma mensagem para o destinatario e outra para o aluno")
    void deveEnviarParaOsDoisDestinatarios() {
        EnviarAvisosWhatsAppUseCase.Resultado resultado = useCase.executar(AVISO);

        List<WhatsAppServicePortSpy.Envio> envios = porta.envios();

        assertEquals(2, envios.size());
        assertEquals("+551199999888", envios.get(0).destino().getValor());
        assertEquals("+5511977776666", envios.get(1).destino().getValor());
        assertEquals(AVISO.mensagemParaDestinatario(), envios.get(0).mensagem());
        assertEquals(AVISO.mensagemParaAluno(), envios.get(1).mensagem());

        assertEquals(2, resultado.envios().size());
        assertEquals("SM1SPY", resultado.envios().get(0).identificadorMensagem());
        assertEquals("SM2SPY", resultado.envios().get(1).identificadorMensagem());
    }

    @Test
    @DisplayName("interrompe no primeiro envio que falhar")
    void deveInterromperNoPrimeiroEnvioQueFalhar() {
        porta.falharNoPrimeiroEnvioCom(new FalhaEnvioWhatsAppException("credencial expirada"));

        assertThrows(FalhaEnvioWhatsAppException.class, () -> useCase.executar(AVISO));
        assertEquals(1, porta.envios().size());
    }
}
