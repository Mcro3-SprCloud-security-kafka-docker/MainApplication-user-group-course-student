package com.quanzip.filemvc.service.dto;

import java.time.LocalDateTime;

public class PercentDTO {
    private Long id;
    private Integer category;
    private String subject;
    private Double value;
    private LocalDateTime localDateTime;

    public PercentDTO() {
    }

    public PercentDTO(Long id, Integer category, String subject, Double value, LocalDateTime localDateTime) {
        this.id = id;
        this.category = category;
        this.subject = subject;
        this.value = value;
        this.localDateTime = localDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
