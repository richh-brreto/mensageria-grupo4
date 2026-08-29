package com.escola.notification.domain.ports;

import com.escola.notification.domain.entities.Lesson;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepositoryPort {

    List<Lesson> findAll();

    List<Lesson> findUpcomingLessons(LocalDate date);

}
