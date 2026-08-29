package com.escola.notification.usecases.impl;

import com.escola.notification.domain.entities.Lesson;
import com.escola.notification.domain.ports.LessonRepositoryPort;
import com.escola.notification.usecases.FindLessonsUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FindLessonsUseCaseImpl implements FindLessonsUseCase {

    private final LessonRepositoryPort lessonRepositoryPort;

    public FindLessonsUseCaseImpl(LessonRepositoryPort lessonRepositoryPort) {
        this.lessonRepositoryPort = lessonRepositoryPort;
    }

    @Override
    public List<Lesson> findAll() {
        return lessonRepositoryPort.findAll();
    }

    @Override
    public List<Lesson> findUpcomingLessons(LocalDate date) {
        return lessonRepositoryPort.findUpcomingLessons(date);
    }
}
