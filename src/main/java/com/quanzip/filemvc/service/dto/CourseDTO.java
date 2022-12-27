package com.quanzip.filemvc.service.dto;

import java.util.List;

public class CourseDTO extends BaseEntityDTO{
    private Long id;
    private String name;
    private List<ScoreDTO> scoreDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScoreDTO> getScoreDTOS() {
        return scoreDTOS;
    }

    public void setScoreDTOS(List<ScoreDTO> scoreDTOS) {
        this.scoreDTOS = scoreDTOS;
    }
}
