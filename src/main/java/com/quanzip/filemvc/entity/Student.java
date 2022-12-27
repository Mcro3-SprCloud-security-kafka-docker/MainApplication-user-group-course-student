package com.quanzip.filemvc.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "student")
@EnableJpaAuditing
public class Student extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    @OneToOne
//    @PrimaryKeyJoinColumn //if we use this, then this field(user_id) will disappear in the table
//    there are only two fields in the created tables(id and code) and the value
//    focus: of id will be equal to value of user id corresponding
    private User user;

//    Cos the co hooac khong
    @OneToMany(mappedBy = "student")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private List<Score> scores;

//    Nếu như Score không có trường Double score, thì chỉ có 2 trường là ID, student_id, Course_id
//    Thì ta có thể không cần tạo entity Score nhưng trong Db vẫn có bảng Score bằng Cách sau:
//    Chỉ cso thể áp dụng nếu bảng score trung gian này chỉ có 2 cột id như trên và PK ID
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name =  "score", joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "score_id"))
//    List<Score> scores;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String name) {
        this.code = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
