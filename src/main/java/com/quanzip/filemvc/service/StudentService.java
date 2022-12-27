package com.quanzip.filemvc.service;

import com.quanzip.filemvc.service.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAllStudent(String search, String searchLike);

    void addStudent(StudentDTO studentDTO) throws Exception;

    StudentDTO findStudentById(Long id) throws Exception;

    void updateStudent(StudentDTO studentDTO) throws Exception;

    void deleteUserById(Long id) throws Exception;

}
