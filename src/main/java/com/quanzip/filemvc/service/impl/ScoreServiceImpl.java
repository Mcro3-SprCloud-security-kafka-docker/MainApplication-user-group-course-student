package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.entity.Score;
import com.quanzip.filemvc.entity.Student;
import com.quanzip.filemvc.repository.ScoreRepository;
import com.quanzip.filemvc.service.RoleService;
import com.quanzip.filemvc.service.ScoreService;
import com.quanzip.filemvc.service.StudentService;
import com.quanzip.filemvc.service.dto.CourseDTO;
import com.quanzip.filemvc.service.dto.ScoreDTO;
import com.quanzip.filemvc.service.dto.StudentDTO;
import com.quanzip.filemvc.service.mapper.ScoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.Objects;

@Service
public class ScoreServiceImpl implements ScoreService {
    private final Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseServiceImpl courseService;

    @Override
    public void saveScore(ScoreDTO scoreDTO) throws Exception {
        if(Objects.isNull( scoreDTO)) throw new Exception("Can not save a null score");
        Score savedScore = scoreRepository.save(scoreMapper.toEntity(scoreDTO));
        StudentDTO studentDTO = studentService.findStudentById(savedScore.getStudent().getId());
        CourseDTO courseDTO = courseService.findCourseById(savedScore.getCourse().getId());
        logger.info("Saved score of student code: "
                + studentDTO.getCode()
                + ", course name: "+ courseDTO.getName());
    }

    @Override
    public void deleteByScoreId(Long scoreId) throws Exception {
        if(Objects.isNull(scoreId)) throw new Exception("Can not save a null score");
        Score score = scoreRepository.findById(scoreId).orElseThrow(NoResultException::new);
//        get student and Course by Score to log
        StudentDTO studentDTO = studentService.findStudentById(score.getStudent().getId());
        CourseDTO courseDTO = courseService.findCourseById(score.getCourse().getId());

        scoreRepository.deleteById(scoreId);
        logger.info("Delete score id:" + scoreId + ", course: " + courseDTO.getName()
                + " of student code: " + studentDTO.getCode());
    }
}
