package com.quanzip.filemvc.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "birthday")
public class BirthDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String userLogin;

    private LocalDate birthDate;
    
    private boolean isMailSent;

    public BirthDay(Long id, String userName, String userLogin, LocalDate birthDate, boolean isMailSent) {
        this.id = id;
        this.userName = userName;
        this.userLogin = userLogin;
        this.birthDate = birthDate;
        this.isMailSent = isMailSent;
    }

    public boolean isMailSent() {
        return isMailSent;
    }

    public void setMailSent(boolean mailSent) {
        isMailSent = mailSent;
    }

    public BirthDay() {
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
