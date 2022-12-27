package com.quanzip.filemvc.entity;

import javax.persistence.*;

@Entity
@Table(name = "score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // vi là có thêm cột này nên phải tạo thêm 1 entity score này,Nếu như không có điểm ở mỗi môn học,
//    Mk chỉ cần khai báo @ManytoMany như ở bên Student
    private Double score;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
