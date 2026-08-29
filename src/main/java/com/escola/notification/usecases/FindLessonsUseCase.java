package com.escola.notification.usecases;

import com.escola.notification.domain.entities.Lesson;

import java.time.LocalDate;
import java.util.List;

public interface FindLessonsUseCase {
    List<Lesson> findAll();
    List<Lesson> findUpcomingLessons(LocalDate date);
}
