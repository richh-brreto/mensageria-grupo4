package com.escola.notification.adapters.mappers;

import com.escola.notification.domain.entities.Lesson;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

/**
 * Mapper para converter objetos/DTOs do banco e mensageria em entidades de domínio.
 * Mantém a camada de domínio livre de dependências externas.
 */
@Component
public class LessonMapper {

    public LessonMapper() {
    }

    public Lesson toDomain(String id, OffsetDateTime scheduledAt, String teacherId) {
        return new Lesson(id, scheduledAt, teacherId);
    }
}
