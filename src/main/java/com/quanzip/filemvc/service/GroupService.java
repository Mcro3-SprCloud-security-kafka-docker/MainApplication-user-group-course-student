package com.quanzip.filemvc.service;

import com.quanzip.filemvc.service.dto.GroupDTO;
import com.quanzip.filemvc.service.dto.UserDTO;

import java.util.List;

public interface GroupService {
    List<GroupDTO> findAllGroups(String search);

    GroupDTO addGroup(GroupDTO groupDTO) throws Exception;

    List<UserDTO> updateGroup(Long id) throws Exception;

    GroupDTO findGroupById(Long id);

    void deleteGroupById(Long id);

    void addGroupUser(Long groupId, Long userId) throws Exception;

    void deletebyGroupIdAndUserId(Long groupId, Long userId) throws Exception;
}
