package com.escola.notification.domain.ports;

import com.escola.notification.domain.model.LessonNotificationEvent;

public interface EventPublisherPort {
    void publicar(LessonNotificationEvent evento);
}
