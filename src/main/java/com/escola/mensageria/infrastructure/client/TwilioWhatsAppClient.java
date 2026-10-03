package com.escola.mensageria.infrastructure.client;

import com.escola.mensageria.infrastructure.config.WhatsAppProperties;
import com.escola.mensageria.infrastructure.exception.WhatsAppClientException;
import com.twilio.exception.TwilioException;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import java.util.function.Supplier;

public class TwilioWhatsAppClient {

    private static final String PREFIXO_WHATSAPP = "whatsapp:";

    private final WhatsAppProperties properties;
    private final Supplier<TwilioRestClient> clientFactory;

    private volatile TwilioRestClient restClient;

    public TwilioWhatsAppClient(WhatsAppProperties properties) {
        this(properties, () -> new TwilioRestClient.Builder(
                properties.getAccountSid(),
                properties.getAuthToken()
        ).build());
    }

    TwilioWhatsAppClient(WhatsAppProperties properties, Supplier<TwilioRestClient> clientFactory) {
        this.properties = properties;
        this.clientFactory = clientFactory;
    }

    public EnvioBruto enviar(String destinoComPrefixoWhatsApp, String corpo) {
        validarCredenciais();

        String messagingServiceSid = properties.getMessagingServiceSid();
        String remetente = properties.getRemetente();
        validarRemetente(messagingServiceSid, remetente);

        try {
            MessageCreator creator = criarMessageCreator(destinoComPrefixoWhatsApp, corpo, messagingServiceSid, remetente);
            Message mensagem = creator.create(obterRestClient());
            return new EnvioBruto(mensagem.getSid(), statusDe(mensagem));
        } catch (TwilioException excecao) {
            throw new WhatsAppClientException("O Twilio rejeitou o envio da mensagem: " + excecao.getMessage(), excecao);
        }
    }

    private MessageCreator criarMessageCreator(String destino, String corpo,
                                              String messagingServiceSid, String remetente) {
        PhoneNumber para = new PhoneNumber(destino);

        MessageCreator creator = preenchido(messagingServiceSid)
                ? Message.creator(para, messagingServiceSid, corpo)
                : Message.creator(para, new PhoneNumber(comPrefixoWhatsApp(remetente)), corpo);

        if (preenchido(properties.getContentSid())) {
            creator.setContentSid(properties.getContentSid());
        }

        return creator;
    }

    private void validarCredenciais() {
        if (!preenchido(properties.getAccountSid()) || !preenchido(properties.getAuthToken())) {
            throw new WhatsAppClientException(
                    "Informe 'app.whatsapp.account-sid' e 'app.whatsapp.auth-token' "
                            + "para se autenticar no Twilio."
            );
        }
    }

    private void validarRemetente(String messagingServiceSid, String remetente) {
        if (!preenchido(messagingServiceSid) && !preenchido(remetente)) {
            throw new WhatsAppClientException(
                    "Informe 'app.whatsapp.messaging-service-sid' ou 'app.whatsapp.remetente' "
                            + "para identificar o numero remetente."
            );
        }
    }

    private TwilioRestClient obterRestClient() {
        TwilioRestClient local = restClient;
        if (local != null) {
            return local;
        }

        synchronized (this) {
            if (restClient == null) {
                restClient = clientFactory.get();
            }
            return restClient;
        }
    }

    private static String comPrefixoWhatsApp(String numero) {
        return numero.startsWith(PREFIXO_WHATSAPP) ? numero : PREFIXO_WHATSAPP + numero;
    }

    private static String statusDe(Message mensagem) {
        return mensagem.getStatus() == null ? null : mensagem.getStatus().name();
    }

    private static boolean preenchido(String valor) {
        return valor != null && !valor.isBlank();
    }

    public record EnvioBruto(String sid, String status) {
    }
}
