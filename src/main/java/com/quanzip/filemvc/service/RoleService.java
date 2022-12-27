package com.quanzip.filemvc.service;

import com.quanzip.filemvc.service.dto.RoleDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface RoleService {
    void deleteByUserId(Long userId) throws Exception;

    List<RoleDTO> searchAndGetRole(String search, Pageable pageable);

    void deleleRoleAllUserByRoleId(Long roleId) throws Exception;

    void addRole(RoleDTO roleDTO) throws Exception;
}
