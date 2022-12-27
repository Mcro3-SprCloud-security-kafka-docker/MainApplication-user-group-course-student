package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.service.dto.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ScoreMapper.class})
public interface CourseMapper extends EntityMapper<CourseDTO, Course>{
    @Mapping(source = "scores", target = "scoreDTOS")
    CourseDTO toDto(Course course);

    @Mapping(source = "scoreDTOS", target="scores")
    Course toEntity(CourseDTO courseDTO);
}
