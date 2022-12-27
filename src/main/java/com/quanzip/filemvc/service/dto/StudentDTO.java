package com.quanzip.filemvc.service.dto;

import com.quanzip.filemvc.entity.Score;
import com.quanzip.filemvc.entity.User;
import lombok.Data;

import java.util.List;

//@Data
public class StudentDTO extends BaseEntityDTO{
    private Long id;
    private String code;
    private Long userId;
    private String userName;
    private List<ScoreDTO> scoreDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ScoreDTO> getScoreDTOS() {
        return scoreDTOS;
    }

    public void setScoreDTOS(List<ScoreDTO> scoreDTOS) {
        this.scoreDTOS = scoreDTOS;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
