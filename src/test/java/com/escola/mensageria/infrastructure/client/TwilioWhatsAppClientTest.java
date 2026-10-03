package com.escola.mensageria.infrastructure.client;

import com.escola.mensageria.infrastructure.config.WhatsAppProperties;
import com.escola.mensageria.infrastructure.exception.WhatsAppClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TwilioWhatsAppClientTest {

    private WhatsAppProperties properties;

    @BeforeEach
    void setUp() {
        properties = new WhatsAppProperties();
        properties.setAccountSid("AC123");
        properties.setAuthToken("token123");
    }

    @Test
    @DisplayName("exige credenciais configuradas")
    void deveExigirCredenciais() {
        properties.setAccountSid("  ");
        TwilioWhatsAppClient client = new TwilioWhatsAppClient(properties);

        WhatsAppClientException excecao = assertThrows(
                WhatsAppClientException.class,
                () -> client.enviar("whatsapp:+5511988888888", "Ola")
        );

        org.junit.jupiter.api.Assertions.assertTrue(excecao.getMessage().contains("account-sid"));
    }

    @Test
    @DisplayName("exige um remetente identificavel (numero ou messaging service)")
    void deveExigirRemetente() {
        TwilioWhatsAppClient client = new TwilioWhatsAppClient(properties);

        WhatsAppClientException excecao = assertThrows(
                WhatsAppClientException.class,
                () -> client.enviar("whatsapp:+5511988888888", "Ola")
        );

        org.junit.jupiter.api.Assertions.assertTrue(excecao.getMessage().contains("remetente"));
    }
}
