package com.escola.notification.usecases;

import com.escola.notification.domain.model.LessonNotificationEvent;

public interface SendLessonNotificationEmailUseCase {
    void processar(LessonNotificationEvent evento);
}
