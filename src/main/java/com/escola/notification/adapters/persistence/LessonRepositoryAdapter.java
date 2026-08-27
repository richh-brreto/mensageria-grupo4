package com.escola.notification.adapters.persistence;

import com.escola.notification.adapters.mappers.LessonMapper;
import com.escola.notification.domain.entities.Lesson;
import com.escola.notification.usecases.ports.output.LessonRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LessonRepositoryAdapter implements LessonRepositoryPort {

    private final LessonJpaRepository lessonJpaRepository;
    private final LessonMapper lessonMapper;

    public LessonRepositoryAdapter(LessonJpaRepository lessonJpaRepository, LessonMapper lessonMapper) {
        this.lessonJpaRepository = lessonJpaRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public List<Lesson> findAll() {
        return lessonJpaRepository.findAll()
                .stream()
                .map(lessonMapper::toDomain)
                .toList();
    }

    @Override
    public List<Lesson> findUpcomingLessons(LocalDate date) {
        return lessonJpaRepository.findByDate(date)
                .stream()
                .map(lessonMapper::toDomain)
                .toList();
    }
}
