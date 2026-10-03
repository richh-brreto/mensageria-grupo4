package com.escola.mensageria.adapters.outbound;

import com.escola.mensageria.domain.exception.FalhaEnvioWhatsAppException;
import com.escola.mensageria.domain.gateway.WhatsAppServicePort;
import com.escola.mensageria.domain.valueobject.Telefone;
import com.escola.mensageria.infrastructure.client.TwilioWhatsAppClient;
import com.escola.mensageria.infrastructure.exception.WhatsAppClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TwilioWhatsAppServiceAdapterTest {

    private static final Telefone DESTINO = Telefone.de("11988888888");

    @Mock
    private TwilioWhatsAppClient twilioWhatsAppClient;

    private TwilioWhatsAppServiceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TwilioWhatsAppServiceAdapter(twilioWhatsAppClient);
    }

    @Test
    @DisplayName("encaminha o destino ja com o prefixo whatsapp: exigido pelo Twilio")
    void deveEncaminharDestinoComPrefixoWhatsApp() {
        when(twilioWhatsAppClient.enviar(anyString(), anyString()))
                .thenReturn(new TwilioWhatsAppClient.EnvioBruto("SM123", "queued"));

        WhatsAppServicePort.ResultadoEnvio resultado = adapter.enviar(DESTINO, "Ola");

        assertEquals("SM123", resultado.identificador());
        assertEquals(WhatsAppServicePort.Situacao.ENVIADA, resultado.situacao());
        verify(twilioWhatsAppClient).enviar("whatsapp:+5511988888888", "Ola");
    }

    @Test
    @DisplayName("traduz falha do SDK em excecao de dominio")
    void deveTraduzirFalhaDoSdk() {
        when(twilioWhatsAppClient.enviar(anyString(), anyString()))
                .thenThrow(new WhatsAppClientException("401 Unauthorized"));

        FalhaEnvioWhatsAppException excecao =
                assertThrows(FalhaEnvioWhatsAppException.class, () -> adapter.enviar(DESTINO, "Ola"));

        assertEquals("Nao foi possivel enviar a mensagem de WhatsApp para +5511988888888.", excecao.getMessage());
    }
}
