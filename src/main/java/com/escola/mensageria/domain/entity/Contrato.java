package com.escola.mensageria.domain.entity;

import java.time.LocalDate;

public record Contrato(Long id, String tipo, LocalDate dataInicio, LocalDate dataFim) {
}
