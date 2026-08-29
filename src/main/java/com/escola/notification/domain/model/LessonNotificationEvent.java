package com.escola.notification.domain.model;

import java.time.Instant;
import java.util.UUID;

public record LessonNotificationEvent(
        UUID eventId,
        String eventType,
        Instant timestamp,
        LessonNotificationPayload payload
) {
    public static final String EVENT_TYPE = "NOTIFICAR_AULA";
}
