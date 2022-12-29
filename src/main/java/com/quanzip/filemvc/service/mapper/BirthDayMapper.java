package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.BirthDay;
import com.quanzip.filemvc.service.dto.BirthDayDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {}, componentModel = "spring")
public interface BirthDayMapper extends EntityMapper<BirthDayDTO, BirthDay> {
}
