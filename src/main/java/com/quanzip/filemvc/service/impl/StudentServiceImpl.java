package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.entity.Student;
import com.quanzip.filemvc.repository.StudentRepository;
import com.quanzip.filemvc.service.RoleService;
import com.quanzip.filemvc.service.StudentService;
import com.quanzip.filemvc.service.dto.StudentDTO;
import com.quanzip.filemvc.service.mapper.StudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<StudentDTO> getAllStudent(String search, String searchLike) {
        if(Objects.isNull(search) || search.isEmpty()) return studentMapper.toDtos(studentRepository.findAll());
        List<StudentDTO> result = studentMapper.toDtos(studentRepository.findAllStudentByIgnoreCaseNameAndNameLike(search, searchLike));
        return result;
    }

    @Override
    public void addStudent(StudentDTO studentDTO) throws Exception {
        if(Objects.isNull(studentDTO)) throw new Exception("Can not save a null student");
        Student student = studentRepository.save(studentMapper.toEntity(studentDTO));
        logger.info("Saved student, code: " + student.getCode());
    }

    @Override
    public StudentDTO findStudentById(Long id) throws Exception {
        if(Objects.isNull(id)) throw new Exception("id can not be null to get student");
        return studentMapper.toDto(studentRepository.findById(id).orElseThrow(()-> new NoResultException()));
    }

    @Override
    public void updateStudent(StudentDTO studentDTO) throws Exception {
        if(Objects.isNull(studentDTO)) throw new Exception("Can not save a null student");
        Student savedStudent = studentRepository.save(studentMapper.toEntity(studentDTO));
        logger.info("Saved student: " + savedStudent.getCode() +
                (Objects.isNull(studentDTO.getUserName()) ? "" : (", name: " + studentDTO.getUserName())));
    }

    @Override
    public void deleteUserById(Long id) throws Exception {
        if(Objects.isNull(id)) throw new Exception("Can not save a null student");
        studentRepository.deleteById(studentRepository.findById(id).orElseThrow(()-> new NoResultException()).getId());
        logger.info("Deleted student id: " + id);
    }

}
