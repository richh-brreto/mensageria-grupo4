package com.escola.notification.adapters.persistence;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "aula")
public class LessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Integer id;

    @Column(name = "data", nullable = false)
    private LocalDate date;

    @Column(name = "presenca")
    private Boolean presence;

    @Column(name = "status")
    private String status;

    @Column(name = "hora_inicio")
    private LocalTime startTime;

    @Column(name = "hora_fim")
    private LocalTime endTime;

    @Column(name = "contrato_id_contrato", nullable = false)
    private Integer contractId;


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
