package com.escola.notification.domain.model;

import java.util.List;

public record LessonNotificationPayload(
        Long aulaId,
        String dataAula,
        String horario,
        Participant professor,
        List<Participant> alunos
) {
}
