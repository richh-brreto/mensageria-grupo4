package com.escola.notification.domain.entities;

import java.time.OffsetDateTime;

/**
 * Entidade de domínio que representa uma aula.
 * Sem dependências de frameworks.
 */
public final class Lesson {
    private final String id;
    private final OffsetDateTime scheduledAt;
    private final String teacherId;

    public Lesson(String id, OffsetDateTime scheduledAt, String teacherId) {
        this.id = id;
        this.scheduledAt = scheduledAt;
        this.teacherId = teacherId;
    }

    public String getId() {
        return id;
    }

    public OffsetDateTime getScheduledAt() {
        return scheduledAt;
    }

    public String getTeacherId() {
        return teacherId;
    }
}
