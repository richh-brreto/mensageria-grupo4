package com.escola.notification.adapters.mappers;

import com.escola.notification.adapters.persistence.LessonEntity;
import com.escola.notification.domain.entities.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {

    public Lesson toDomain(LessonEntity entity) {
        return new Lesson(
                entity.getId(),
                entity.getDate(),
                entity.getPresence(),
                entity.getStatus(),
                entity.getStartTime(),
                entity.getEndTime(),
                entity.getContractId()
        );
    }

    public LessonEntity toEntity(Lesson lesson) {
        LessonEntity entity = new LessonEntity();
        entity.setId(lesson.getId());
        entity.setDate(lesson.getDate());
        entity.setPresence(lesson.getPresence());
        entity.setStatus(lesson.getStatus());
        entity.setStartTime(lesson.getStartTime());
        entity.setEndTime(lesson.getEndTime());
        entity.setContractId(lesson.getContractId());
        return entity;
    }
}
