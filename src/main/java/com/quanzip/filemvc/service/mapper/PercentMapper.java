package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.Percent;
import com.quanzip.filemvc.service.dto.PercentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface PercentMapper extends EntityMapper<PercentDTO, Percent> {

}
