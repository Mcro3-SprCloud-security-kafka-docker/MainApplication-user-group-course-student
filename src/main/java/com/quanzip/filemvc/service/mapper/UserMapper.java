package com.quanzip.filemvc.service.mapper;

import com.quanzip.filemvc.entity.User;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, StudentMapper.class})
public interface UserMapper extends EntityMapper<UserDTO, User> {
    @Mapping(source = "roleDTOList", target = "roles")
    @Mapping(source = "studentDTO", target="student")
    User toEntity(UserDTO userDTO);

    @Mapping(source = "roles", target="roleDTOList")
    @Mapping(source = "student", target = "studentDTO")
    UserDTO toDto(User user);

    default User fromId(Long id){
        if(Objects.isNull(id)) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}
