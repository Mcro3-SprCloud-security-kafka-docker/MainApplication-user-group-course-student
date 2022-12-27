package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.entity.Group;
import com.quanzip.filemvc.entity.GroupUser;
import com.quanzip.filemvc.repository.GroupRepository;
import com.quanzip.filemvc.repository.GroupUserRepositoryImpl;
import com.quanzip.filemvc.service.GroupService;
import com.quanzip.filemvc.service.RoleService;
import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.GroupDTO;
import com.quanzip.filemvc.service.dto.UserDTO;
import com.quanzip.filemvc.service.mapper.GroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupUserRepositoryImpl groupUserRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<GroupDTO> findAllGroups(String search) {
        if(Objects.isNull(search) || search.isEmpty()) {
            return groupMapper.toDtos(groupRepository.findAll());
        }
        List<GroupDTO> groupDTOS = groupMapper.toDtos(groupRepository.findAllByNameLikeOrNameIgnoreCase("%"+search+"%", search));
        return groupDTOS;
    }

    @Override
    public GroupDTO addGroup(GroupDTO groupDTO) throws Exception {
        if(Objects.isNull(groupDTO)) throw new Exception("Can not save a null group");
        if(groupDTO.getName() == null || groupDTO.getName().isEmpty()) return null;
        Group group = groupMapper.toEntity(groupDTO);
        GroupDTO result = groupMapper.toDto(groupRepository.save(group));
        return result;
    }

    @Override
    public List<UserDTO> updateGroup(Long id) throws Exception {
        groupRepository.findById(id).orElseThrow(NoResultException::new);

        // get row that has groupId in groupsUser
        List<GroupUser> groupUsers = groupUserRepository.getGroupUserByGroupId(id);
        // Get users belong to each groupId
        List<UserDTO> userDTOList = groupUsers.stream().map(groupUser -> {
            Long userId = groupUser.getUserId();
            UserDTO userDTO;
            try {
                userDTO = userService.findUserById(userId);
            } catch (Exception e) {
                userDTO = new UserDTO();
                e.printStackTrace();
            }
            return userDTO;
        }).collect(Collectors.toList());
        return userDTOList;
    }

    @Override
    public GroupDTO findGroupById(Long id) {
        return groupMapper.toDto(groupRepository.findById(id).orElseThrow(NoResultException::new));
    }

    @Override
    public void deleteGroupById(Long id) {
        groupRepository.findById(id).orElseThrow(NoResultException::new);
        groupRepository.deleteById(id);
    }

    @Override
    public void addGroupUser(Long groupId, Long userId) throws Exception {
        Group group = groupRepository.findById(groupId).orElseThrow(NoResultException::new);
        UserDTO userDTO = userService.findUserById(userId);
        groupUserRepository.insertGroupUser(group, userDTO);
    }

    @Override
    public void deletebyGroupIdAndUserId(Long groupId, Long userId) throws Exception {
        groupRepository.findById(groupId).orElseThrow(NoResultException::new);
        userService.findUserById(userId);
        logger.info("Deleting group-user: " + groupId + ", " + userId);
        groupUserRepository.delete(groupId, userId);
    }
}
