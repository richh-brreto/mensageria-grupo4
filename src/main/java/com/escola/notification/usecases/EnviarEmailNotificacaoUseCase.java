package com.escola.notification.usecases;

import com.escola.notification.domain.model.AulaNotificacaoEvent;

public interface EnviarEmailNotificacaoUseCase {
    void processar(AulaNotificacaoEvent evento);
}
