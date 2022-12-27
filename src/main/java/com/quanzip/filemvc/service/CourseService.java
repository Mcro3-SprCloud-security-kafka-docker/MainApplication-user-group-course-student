package com.quanzip.filemvc.service;

import com.quanzip.filemvc.service.dto.CourseDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> findAllCourse(String search, String searchLike);

    void addCourse(CourseDTO courseDTO) throws Exception;

    CourseDTO findCourseById(Long id) throws Exception;

    void updateCourse(CourseDTO courseDTO) throws Exception;

    void deleteCourseById(Long id) throws Exception;
}