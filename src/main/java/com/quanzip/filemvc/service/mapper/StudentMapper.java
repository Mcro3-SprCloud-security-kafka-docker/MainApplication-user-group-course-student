package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.Student;
import com.quanzip.filemvc.service.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ScoreMapper.class})
public interface StudentMapper extends EntityMapper<StudentDTO, Student>{
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "scores", target = "scoreDTOS")
    @Mapping(source = "user.userName", target = "userName")
    StudentDTO toDto(Student student);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "scoreDTOS", target="scores")
    Student toEntity(StudentDTO studentDTO);


}
