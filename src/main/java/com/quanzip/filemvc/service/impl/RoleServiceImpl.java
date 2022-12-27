package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.entity.Role;
import com.quanzip.filemvc.repository.RoleRepository;
import com.quanzip.filemvc.service.RoleService;
import com.quanzip.filemvc.service.dto.RoleDTO;
import com.quanzip.filemvc.service.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public void deleteByUserId(Long userId) throws Exception {
        if(Objects.isNull(userId)) throw new Exception("Can not delete role when userId = null");
        roleRepository.deleteByUserId(userId);
    }

    @Override
    public List<RoleDTO> searchAndGetRole(String search, Pageable pageable) {
        if(Objects.isNull(search) || search.isEmpty()) {
            Page<Role> roles = roleRepository.findAll(pageable);
            List<RoleDTO> roleDTOS = roleMapper.toDtos(roles.getContent());
            return roleDTOS;
        }
        Page<Role> roles = roleRepository.findAllByRoleLikeOrRoleIgnoreCase("%"+search+"%", search, pageable);
        List<RoleDTO> roleDTOS = roleMapper.toDtos(roles.getContent());
        return roleDTOS;
    }

    @Override
    public void deleleRoleAllUserByRoleId(Long roleId) throws Exception {
        if(Objects.isNull(roleId)) throw new Exception("Can not delete role with null ID");
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()-> new NoResultException("No role found by id: "+  roleId));
        roleRepository.delete(role);
    }

    @Override
    public void addRole(RoleDTO roleDTO) throws Exception {
        if(Objects.isNull(roleDTO)) throw new Exception("Can not save null role");
        if(roleRepository.countAllByRoleAndUser_Id(roleDTO.getRole(), roleDTO.getUserId()) > 0){
            return;
        }
        Role role = roleMapper.toEntity(roleDTO);
        roleRepository.save(role);
    }
}
