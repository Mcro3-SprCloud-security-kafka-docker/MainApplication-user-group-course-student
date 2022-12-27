package com.quanzip.filemvc.service.dto;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.entity.Student;
import lombok.Data;

public class ScoreDTO extends BaseEntityDTO{
    private Long id;
    private Double score;
    private Long studentId;
    private Long courseId;
    private String courseName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
