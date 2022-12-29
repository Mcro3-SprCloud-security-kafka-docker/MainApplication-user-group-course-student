package com.quanzip.filemvc.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class BirthDayDTO {
    private Long id;
    private String userName;
    private String userLogin;

    @DateTimeFormat(pattern = "dd/MM/yyyy")

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
//    -1 daqua, 0: hnay, 1: sap den
    private int status;

    private boolean isMailSent;

    public BirthDayDTO() {
    }

    public boolean isMailSent() {
        return isMailSent;
    }

    public void setMailSent(boolean mailSent) {
        isMailSent = mailSent;
    }

    public BirthDayDTO(Long id, String userName, String userLogin, LocalDate birthDate, int status, boolean isMailSent) {
        this.id = id;
        this.userName = userName;
        this.userLogin = userLogin;
        this.birthDate = birthDate;
        this.status = status;
        this.isMailSent = isMailSent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
