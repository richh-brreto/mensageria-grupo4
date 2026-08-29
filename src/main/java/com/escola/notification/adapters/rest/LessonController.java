package com.escola.notification.adapters.rest;

import com.escola.notification.domain.entities.Lesson;
import com.escola.notification.usecases.FindLessonsUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/aulas"})
public class LessonController {

    private final FindLessonsUseCase findLessonsUseCase;

    public LessonController(FindLessonsUseCase findLessonsUseCase) {
        this.findLessonsUseCase = findLessonsUseCase;
    }

    @GetMapping
    public List<Lesson> getLessons(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            return findLessonsUseCase.findAll();
        }

        return findLessonsUseCase.findUpcomingLessons(date);
    }
}
