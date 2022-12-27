package com.quanzip.filemvc.service.dto;

import lombok.Data;

import java.util.List;

public class GroupDTO extends BaseEntityDTO{
    private Long id;
    private String name;
    private List<UserDTO> userDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserDTO> getUserDTOS() {
        return userDTOS;
    }

    public void setUserDTOS(List<UserDTO> userDTOS) {
        this.userDTOS = userDTOS;
    }
}
