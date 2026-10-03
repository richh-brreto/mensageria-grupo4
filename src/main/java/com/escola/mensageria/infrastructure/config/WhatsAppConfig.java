package com.escola.mensageria.infrastructure.config;

import com.escola.mensageria.infrastructure.client.TwilioWhatsAppClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WhatsAppProperties.class)
public class WhatsAppConfig {

    @Bean
    public TwilioWhatsAppClient twilioWhatsAppClient(WhatsAppProperties properties) {
        return new TwilioWhatsAppClient(properties);
    }
}
