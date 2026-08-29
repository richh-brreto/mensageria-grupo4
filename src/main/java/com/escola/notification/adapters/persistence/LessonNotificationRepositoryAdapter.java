package com.escola.notification.adapters.persistence;

import com.escola.notification.domain.model.LessonNotificationPayload;
import com.escola.notification.domain.model.Participant;
import com.escola.notification.domain.ports.LessonNotificationRepositoryPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LessonNotificationRepositoryAdapter implements LessonNotificationRepositoryPort {

    private static final String SQL = """
        SELECT
            au.id_aula,
            au.data AS data_aula,
            CONCAT(au.hora_inicio, ' - ', au.hora_fim) AS horario,
            COALESCE(p_contrato.nome, p_turma.nome) AS professor_nome,
            COALESCE(p_contrato.email, p_turma.email) AS professor_email,
            GROUP_CONCAT(DISTINCT COALESCE(a_ind.nome, a_tur.nome) SEPARATOR ', ') AS alunos_nomes,
            GROUP_CONCAT(DISTINCT COALESCE(a_ind.email, a_tur.email) SEPARATOR ', ') AS alunos_emails
        FROM aula au
        JOIN contrato c ON au.contrato_id_contrato = c.id_contrato
        LEFT JOIN professor p_contrato ON c.professor_id_professor = p_contrato.id_professor
        LEFT JOIN turma t ON c.turma_id_turma = t.id_turma
        LEFT JOIN professor p_turma ON t.professor_id_professor = p_turma.id_professor
        LEFT JOIN aluno a_ind ON c.aluno_id_aluno = a_ind.id_aluno
        LEFT JOIN disponibilidade_turma dt ON t.id_turma = dt.turma_id_turma
        LEFT JOIN disponibilidade_aluno da ON dt.horario_id_horario = da.horario_id_horario
        LEFT JOIN aluno a_tur ON da.aluno_id_aluno = a_tur.id_aluno
        GROUP BY
            au.id_aula,
            au.data,
            au.hora_inicio,
            au.hora_fim,
            professor_nome,
            professor_email
        """;

    private final JdbcTemplate jdbcTemplate;

    public LessonNotificationRepositoryAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LessonNotificationPayload> buscarAulasParaNotificacao() {
        return jdbcTemplate.query(SQL, (rs, rowNum) -> {
            Long aulaId = rs.getLong("id_aula");
            String dataAula = rs.getString("data_aula");
            String horario = rs.getString("horario");
            String professorNome = rs.getString("professor_nome");
            String professorEmail = rs.getString("professor_email");
            String alunosNomes = rs.getString("alunos_nomes");
            String alunosEmails = rs.getString("alunos_emails");

            Participant professor = professorEmail == null || professorEmail.isBlank()
                    ? null
                    : new Participant(professorNome == null ? "Professor" : professorNome, professorEmail);

            return new LessonNotificationPayload(
                    aulaId,
                    dataAula,
                    horario,
                    professor,
                    parseAlunos(alunosNomes, alunosEmails)
            );
        });
    }

    private List<Participant> parseAlunos(String alunosNomes, String alunosEmails) {
        if (alunosNomes == null && alunosEmails == null) {
            return List.of();
        }

        List<String> nomes = splitValue(alunosNomes);
        List<String> emails = splitValue(alunosEmails);
        List<Participant> alunos = new ArrayList<>();

        for (int i = 0; i < Math.max(nomes.size(), emails.size()); i++) {
            String nome = i < nomes.size() ? nomes.get(i) : "Aluno";
            String email = i < emails.size() ? emails.get(i) : null;

            if (email != null && !email.isBlank()) {
                alunos.add(new Participant(nome, email));
            }
        }

        return alunos;
    }

    private List<String> splitValue(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        String[] parts = value.split(",");
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                result.add(trimmed);
            }
        }
        return result;
    }
}
