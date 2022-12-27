package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT s FROM Student s WHERE s.code = :name or s.code like :nameLike" +
            " or s.user.name = :name or s.user.name like :nameLike " +
            "or s.user.userName = :name or s.user.userName like :nameLike")
    List<Student> findAllStudentByIgnoreCaseNameAndNameLike(@Param(value = "name") String name,
                                                            @Param(value = "nameLike") String nameLike);

    @Query(value = "SELECT std from Student std inner join Score sc on std.id = sc.student.id" +
            " where std.id = :studentId and sc.id = :scoreId")
    List<Student> findByIdAndScoreId(@Param(value = "studentId") Long studentId, @Param(value = "scoreId") Long scoreId);
}
