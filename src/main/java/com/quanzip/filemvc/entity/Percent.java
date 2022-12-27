package com.quanzip.filemvc.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "percent")
public class Percent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    Type of measure
    @Column(name = "category")
    private Integer category;


//    Name of action
    private String subject;

    private Double value;

    @Column(name = "inserted_date")
    private LocalDateTime localDateTime;

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

    public LocalDateTime getInsertedDate() {
        return localDateTime;
    }

    public void setInsertedDate(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
