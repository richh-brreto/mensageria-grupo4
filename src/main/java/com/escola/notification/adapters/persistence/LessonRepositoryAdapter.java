package com.escola.notification.adapters.persistence;

import com.escola.notification.domain.entities.Lesson;
import com.escola.notification.usecases.ports.output.LessonRepositoryPort;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Adapter que faz a ponte entre o banco de dados compartilhado (read-only)
 * e o Use Case. Aqui pode usar Spring Data JPA ou JdbcTemplate.
 * Implementação concreta deixada como esqueleto: métodos devem consultar
 * apenas (não alterar) o banco de agendamentos.
 */
@Component
public class LessonRepositoryAdapter implements LessonRepositoryPort {

    // Inject JPA repository or JdbcTemplate here

    public LessonRepositoryAdapter() {
    }

    @Override
    public List<Lesson> findLessonsBetween(OffsetDateTime start, OffsetDateTime end) {
        // TODO: implementar consulta read-only ao banco compartilhado.
        return List.of();
    }
}
