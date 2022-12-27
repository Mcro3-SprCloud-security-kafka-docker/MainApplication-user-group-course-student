package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.Score;
import com.quanzip.filemvc.service.dto.ScoreDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {})
public interface ScoreMapper extends EntityMapper<ScoreDTO, Score>{
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target="courseName")
    ScoreDTO toDto(Score score);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "courseId", target = "course.id")
    Score toEntity(ScoreDTO scoreDTO);

    default Score fromId(Long id){
        if(Objects.isNull(id)) return null;
        else {
            Score score = new Score();
            score.setId(id);
            return score;
        }
    }
}
