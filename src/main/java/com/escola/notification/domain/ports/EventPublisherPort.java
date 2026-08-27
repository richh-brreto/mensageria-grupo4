package com.escola.notification.domain.ports;

import com.escola.notification.domain.model.AulaNotificacaoEvent;

public interface EventPublisherPort {
    void publicar(AulaNotificacaoEvent evento);
}
