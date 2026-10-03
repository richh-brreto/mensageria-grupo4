package com.escola.mensageria.domain.valueobject;

import com.escola.mensageria.domain.exception.TelefoneInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelefoneTest {

    @ParameterizedTest
    @DisplayName("normaliza numeros locais brasileiros para o formato E.164")
    @CsvSource({
            "11999999999,              +5511999999999",
            "11 99999-9999,           +5511999999999",
            "+55 (11) 99999-9999,     +5511999999999",
            "011999999999,            +5511999999999",
            "0055 11 99999 9999,      +5511999999999",
            "1133334444,              +551133334444",
            "+14155238886,            +14155238886"
    })
    void deveNormalizarParaE164(String entrada, String esperado) {
        assertEquals(esperado, Telefone.de(entrada).getValor());
    }

    @Test
    @DisplayName("ignora zero a esquerda em celular com nove digitos")
    void deveRemoverZeroAEsquerda() {
        assertEquals("+5511999999999", Telefone.de("(11) 99999-9999").getValor());
    }

    @Test
    @DisplayName("prefixa o numero com whatsapp: para uso no Twilio")
    void deveAdicionarPrefixoWhatsApp() {
        assertEquals("whatsapp:+5511999999999", Telefone.de("11999999999").comPrefixoWhatsApp());
    }

    @ParameterizedTest
    @DisplayName("rejeita numeros ausentes ou sem digitos")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "abc", "----"})
    void deveRejeitarEntradasInvalidas(String entrada) {
        assertThrows(TelefoneInvalidoException.class, () -> Telefone.de(entrada));
    }

    @Test
    @DisplayName("rejeita numeros com tamanho fora do E.164")
    void deveRejeitarTamanhoInvalido() {
        assertThrows(TelefoneInvalidoException.class, () -> Telefone.de("1234"));
    }

    @Test
    @DisplayName("considera iguais dois telefones normalizados a partir de formatos diferentes")
    void deveImplementarIgualdadePorValor() {
        assertEquals(Telefone.de("11 99999-9999"), Telefone.de("+5511999999999"));
        assertEquals(Telefone.de("11 99999-9999").hashCode(), Telefone.de("+5511999999999").hashCode());
    }
}
