package com.escola.mensageria.infrastructure.config;

import com.escola.mensageria.domain.gateway.WhatsAppServicePort;
import com.escola.mensageria.usecase.EnviarAvisosWhatsAppUseCase;
import com.escola.mensageria.usecase.impl.EnviarAvisosWhatsAppUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public EnviarAvisosWhatsAppUseCase enviarAvisosWhatsAppUseCase(WhatsAppServicePort whatsAppServicePort) {
        return new EnviarAvisosWhatsAppUseCaseImpl(whatsAppServicePort);
    }
}
