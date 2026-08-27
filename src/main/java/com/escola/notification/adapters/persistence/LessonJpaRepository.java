package com.escola.notification.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LessonJpaRepository extends JpaRepository<LessonEntity, Integer> {
    List<LessonEntity> findByDate(LocalDate date);
}
