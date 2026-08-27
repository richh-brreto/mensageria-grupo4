package com.escola.notification.domain.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Entidade de domínio que representa uma aula.
 * Sem dependências de frameworks.
 */
public final class Lesson {
    private Integer id;
    private LocalDate date;
    private Boolean presence;
    private String status;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer contractId;

    public Lesson(Integer id, LocalDate date, Boolean presence, String status, LocalTime startTime, LocalTime endTime, Integer contractId) {
        this.id = id;
        this.date = date;
        this.presence = presence;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.contractId = contractId;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Boolean getPresence() {
        return presence;
    }

    public String getStatus() {
        return status;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }
}
