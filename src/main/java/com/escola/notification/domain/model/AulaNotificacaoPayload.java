package com.escola.notification.domain.model;

import java.util.List;

public record AulaNotificacaoPayload(
        Long aulaId,
        String dataAula,
        String horario,
        Participante professor,
        List<Participante> alunos
) {
}
