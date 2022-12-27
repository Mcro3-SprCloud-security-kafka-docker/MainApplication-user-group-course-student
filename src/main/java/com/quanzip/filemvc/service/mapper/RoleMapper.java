package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.Role;
import com.quanzip.filemvc.service.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    RoleDTO toDto(Role role);

    @Mapping(source = "userId", target = "user.id")
    Role toEntity(RoleDTO roleDTO);

    default Role fromId(Long id){
        if(Objects.isNull(id)) return null;
        Role role = new Role();
        role.setId(id);
        return role;
    }
}
