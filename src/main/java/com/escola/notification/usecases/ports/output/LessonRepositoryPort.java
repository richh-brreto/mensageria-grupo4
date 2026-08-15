package com.escola.notification.usecases.ports.output;

import com.escola.notification.domain.entities.Lesson;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Output Port para leitura das aulas (read-only).
 * Implementations devem prover acesso para buscar aulas próximas de uma janela de tempo.
 */
public interface LessonRepositoryPort {
    /**
     * Busca aulas agendadas entre start (inclusive) e end (exclusive).
     */
    List<Lesson> findLessonsBetween(OffsetDateTime start, OffsetDateTime end);
}
