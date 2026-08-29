package com.escola.notification.domain.ports;

import com.escola.notification.domain.model.LessonNotificationPayload;

import java.util.List;

public interface LessonNotificationRepositoryPort {
    List<LessonNotificationPayload> buscarAulasParaNotificacao();
}
