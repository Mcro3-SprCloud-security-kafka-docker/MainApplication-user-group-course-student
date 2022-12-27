package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.Group;
import com.quanzip.filemvc.service.dto.GroupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface GroupMapper extends EntityMapper<GroupDTO, Group> {
    @Mapping(source = "users", target="userDTOS")
    GroupDTO toDto(Group group);

    @Mapping(source = "userDTOS", target = "users")
    Group toEntity(GroupDTO groupDTO);
}
