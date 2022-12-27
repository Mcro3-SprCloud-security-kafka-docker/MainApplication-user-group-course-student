package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.repository.CourseRepository;
import com.quanzip.filemvc.service.CourseService;
import com.quanzip.filemvc.service.RoleService;
import com.quanzip.filemvc.service.dto.CourseDTO;
import com.quanzip.filemvc.service.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class CourseServiceImpl implements CourseService {
    private final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<CourseDTO> findAllCourse(String search, String searchLike) {
        if(Objects.isNull(search) || search.isEmpty()) return courseMapper.toDtos(courseRepository.findAll());
        List<CourseDTO> res = courseMapper.toDtos(courseRepository.findAllCourseByNameLikeIgnoreCase(search.toLowerCase(Locale.ROOT)
                , searchLike.toLowerCase(Locale.ROOT)));
        return res;
    }

    @Override
    public void addCourse(CourseDTO courseDTO) throws Exception {
        if(Objects.isNull(courseDTO)) throw new Exception("Can not save a null course");
        Course savedCourse = courseRepository.save(courseMapper.toEntity(courseDTO));
        logger.info("Saved course name: "+ savedCourse.getName());
    }

    @Override
    public CourseDTO findCourseById(Long id) throws Exception {
        if(Objects.isNull(id)) throw new Exception("Can not save a null course");
        return courseMapper.toDto(courseRepository.findById(id).orElseThrow(NoResultException::new));
    }

    @Override
    public void updateCourse(CourseDTO courseDTO) throws Exception {
        if(Objects.isNull(courseDTO)) throw new Exception("Can not save a null course");
        Course savedCourse = courseRepository.save(courseMapper.toEntity(courseDTO));
        logger.info("Saved course name: " + savedCourse.getName());
    }

    @Override
    public void deleteCourseById(Long id) throws Exception {
        if(Objects.isNull(id)) throw new Exception("Can not save a null course");
        courseRepository.deleteById(courseRepository.findById(id).orElseThrow(NoResultException::new).getId());
    }
}
