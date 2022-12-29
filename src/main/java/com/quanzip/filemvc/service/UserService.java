package com.quanzip.filemvc.service;

import com.quanzip.filemvc.entity.User;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.springframework.data.util.Streamable;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    UserDTO addUser(UserDTO userDTO) throws Exception;
    List<UserDTO> searchByName(String name);

    void downloadUserAvatar(String name, HttpServletResponse httpServletResponse) throws Exception;

    void deleteUserById(Long id) throws Exception;

    UserDTO editUserById(Long id) throws Exception;

    void updateUser(UserDTO userDTO) throws Exception;

    UserDTO findUserById(Long userId) throws Exception;

    void editRole(UserDTO userDTO) throws Exception;

    List<UserDTO> findUserWhereNotIn(List<Long> ids);

    List<UserDTO> findUserNotStudent();

    List<UserDTO> fineUserIsOnTheirBirthDay();
}
