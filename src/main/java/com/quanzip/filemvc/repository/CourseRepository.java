package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.entity.Role;
import com.quanzip.filemvc.service.dto.CourseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT c from Course c where lower(c.name) = :search or lower(c.name) like :searchLike")
    List<Course> findAllCourseByNameLikeIgnoreCase(@Param(value = "search") String search
            , @Param(value = "searchLike") String searchLike);
}
