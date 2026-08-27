package com.escola.notification.domain.ports;

import com.escola.notification.domain.model.AulaNotificacaoPayload;

import java.util.List;

public interface AulaRepositoryPort {
    List<AulaNotificacaoPayload> buscarAulasParaNotificacao();
}
