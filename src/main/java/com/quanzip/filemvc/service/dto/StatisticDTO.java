package com.quanzip.filemvc.service.dto;

public class StatisticDTO {
    private Long id;
    private String message;
    private String createBy;
    private Long accountId;
    private String clazz;

    public StatisticDTO() {
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "StatisticDTO{" +
                ", message='" + message + '\'' +
                ", clazz='" + clazz;
    }
}
