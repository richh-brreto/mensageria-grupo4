package com.escola.notification.domain.entities;

import java.time.OffsetDateTime;

/**
 * Entidade de domínio que representa uma notificação a ser enviada.
 */
public final class Notification {
    private final String lessonId;
    private final String teacherId;
    private final String toEmail;
    private final String subject;
    private final String body;
    private final OffsetDateTime scheduledFor;

    public Notification(String lessonId, String teacherId, String toEmail, String subject, String body, OffsetDateTime scheduledFor) {
        this.lessonId = lessonId;
        this.teacherId = teacherId;
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
        this.scheduledFor = scheduledFor;
    }

    public String getLessonId() {
        return lessonId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public OffsetDateTime getScheduledFor() {
        return scheduledFor;
    }
}
