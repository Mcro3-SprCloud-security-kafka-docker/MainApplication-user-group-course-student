package com.quanzip.filemvc.service.dto;

public class MailDTO {
    private Long id;

    private String fromMail;
    private String fromName;
    private String toMail;
    private String toName;
    private String subject;
    private String content;
    private String cc;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToMail() {
        return toMail;
    }

    public void setToMail(String toMail) {
        this.toMail = toMail;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    @Override
    public String toString() {
        return "MailDTO{" +
                "fromMail='" + fromMail + '\'' +
                ", fromName='" + fromName + '\'' +
                ", toMail='" + toMail + '\'' +
                ", toName='" + toName + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
