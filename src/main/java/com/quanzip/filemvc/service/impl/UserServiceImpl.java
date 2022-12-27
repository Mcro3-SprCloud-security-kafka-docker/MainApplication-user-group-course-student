package com.quanzip.filemvc.service.impl;

import com.quanzip.filemvc.entity.Role;
import com.quanzip.filemvc.entity.User;
import com.quanzip.filemvc.repository.RoleRepository;
import com.quanzip.filemvc.repository.UserRepository;
import com.quanzip.filemvc.service.StudentService;
import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.RoleDTO;
import com.quanzip.filemvc.service.dto.StudentDTO;
import com.quanzip.filemvc.service.dto.UserDTO;
import com.quanzip.filemvc.service.mapper.RoleMapper;
import com.quanzip.filemvc.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private Environment environment;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    RoleRepository role;

    @Autowired
    private StudentService studentService;

    @Override
    @Transactional  // if any logic inside this function get failed, data will be rollback at moment nothing did
    public UserDTO addUser(UserDTO userDTO) throws Exception {
        if(userDTO == null) throw new Exception("can not save null user");

        String avatarFolder = environment.getProperty("application.avatar-folder");
        MultipartFile avatarFile = userDTO.getFile();
        boolean hasAvatar = !Objects.isNull(avatarFile);
//      set avatar for user

        String avatarName = "";
        if(hasAvatar) {
            avatarName = System.currentTimeMillis() + "-" + avatarFile.getOriginalFilename();
            userDTO.setAvatar(avatarName);
        }

//         save user to DB
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        logger.info("Save user: " + userDTO.getName() + " successfully to database!");

        List<RoleDTO> roleDTOList = userDTO.getRoleDTOList();
        if(!Objects.isNull(roleDTOList)){
            List<Role> roles = roleDTOList.stream().filter(x -> !Objects.isNull(x) && !Objects.isNull(x.getRole()))
                    .peek(roleDTO -> roleDTO.setUserId(savedUser.getId())).map(roleDTO -> roleMapper.toEntity(roleDTO))
                    .collect(Collectors.toList());
            roleRepository.saveAll(roles);
            logger.info("Save : " + userDTO.getName() + "'s roles successfully to database!");
        }

//         save avatar to folder
        if(hasAvatar) {
            File avatarPathFile = new File(avatarFolder + "/" +  avatarName);
            FileOutputStream fileOutputStream = new FileOutputStream(avatarPathFile);
            fileOutputStream.write(avatarFile.getBytes());
            logger.info("Save " + userDTO.getName() + "'s avatar successfully to " + avatarPathFile.getPath());
        }

        return userMapper.toDto(savedUser);
    }

    @Override
    public List<UserDTO> searchByName(String name) {
        if(Objects.isNull(name) || name.isEmpty()) return userMapper.toDtos(userRepository.findAll());
        else{
            List<User> users = userRepository.findAllByUserName(name.toLowerCase())
                    .stream().peek(user -> {
                        List<Role> roles = roleRepository.findAllByUser_Id(user.getId());
                        user.setRoles(roles);
                    }).collect(Collectors.toList());
            return userMapper.toDtos(users);
        }
    }

    @Override
    public void downloadUserAvatar(String name, HttpServletResponse httpServletResponse) throws Exception {
        String avatarFolder = environment.getProperty("application.avatar-folder");
        if(Objects.isNull(name)) return;

        File avatarFile = new File(avatarFolder, name);
        if(!avatarFile.exists()) return;
        httpServletResponse.setContentType("application/octet-stream");
        Files.copy(avatarFile.toPath(), httpServletResponse.getOutputStream());
    }

    @Override
    public void deleteUserById(Long id) throws Exception {
        if(Objects.isNull(id)) throw new Exception("Can not delete user with null ID!");
        userRepository.findById(id).orElseThrow(NoResultException::new);
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO editUserById(Long id) throws Exception {
        if(Objects.isNull(id)) throw new Exception("Can not delete user with null ID!");
        UserDTO userDTO = userMapper.toDto(userRepository.findById(id).orElseThrow(NoResultException::new));
        return userDTO;
    }

    @Override
    public void updateUser(UserDTO userDTO) throws Exception {
        if(Objects.isNull(userDTO)) throw new Exception("Can not delete user with null ID!");
        String avatarFolder = environment.getProperty("application.avatar-folder");

        Long id = userDTO.getId();
        User user = userRepository.findById(id).orElseThrow(NoResultException::new);
        String oldAvatarName = user.getAvatar();
        if(!Objects.isNull(oldAvatarName)){
            File oldAvatarFile = new File(avatarFolder, oldAvatarName);
            if(oldAvatarFile.exists()){
                logger.info("Deleting old avatar file... : " + oldAvatarFile.delete());
            }
        }

        String inputAvatarName = userDTO.getFile().getOriginalFilename();
        String newAvatarImageName = "";
        if(!Objects.isNull(inputAvatarName) && !inputAvatarName.isEmpty()){
           newAvatarImageName = System.currentTimeMillis()+ "-" + inputAvatarName;
           userDTO.setAvatar(newAvatarImageName);
            File newAvatarFile = new File(avatarFolder, newAvatarImageName);
            FileOutputStream fileOutputStream = new FileOutputStream(newAvatarFile);
            fileOutputStream.write(userDTO.getFile().getBytes());
        }
        userRepository.save(userMapper.toEntity(userDTO));

        logger.info("Updating user successfully!");
    }

    @Override
    public UserDTO findUserById(Long userId) throws Exception {
        if(Objects.isNull(userId)) throw new Exception("Can not delete user with null ID!");
        User user = userRepository.findById(userId).orElseThrow(NoResultException::new);
        return userMapper.toDto(user);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void editRole(UserDTO userDTO) throws Exception {
        if(Objects.isNull(userDTO)) throw new Exception("Can not delete user with null ID!");

        // delete old roles of editting user
        Long userId = userDTO.getId();
        userRepository.findById(userId)
                .orElseThrow(()-> new NoResultException("Can not found user by userid: " + userDTO));
        roleRepository.deleteByUserId(userId);

//      get new role just set to add tp Db if set
        List<RoleDTO> roleDTOS = userDTO.getRoleDTOList();
        if(!Objects.isNull(roleDTOS)){
            List<Role> roles = roleMapper.toEntites(roleDTOS.stream()
                    .filter(roleDTO -> !Objects.isNull(roleDTO) && !Objects.isNull(roleDTO.getRole()))
                    .peek(roleDTO -> roleDTO.setUserId(userId))
                    .collect(Collectors.toList()));
            roleRepository.saveAll(roles);
        }

        logger.info("Update "+ userDTO.getName()  + " roles successfully!");
    }

    @Override
    public List<UserDTO> findUserWhereNotIn(List<Long> ids) {
        if(ids.isEmpty()) return userMapper.toDtos(userRepository.findAll());
        List<UserDTO> result = userMapper.toDtos(userRepository.findAllByIdNotIn(ids));
        return result;
    }

    @Override
    public List<UserDTO> findUserNotStudent() {
        List<StudentDTO> studentDTOsThatHasUserAlready = studentService.getAllStudent(null, null);
        List<UserDTO> result = findUserWhereNotIn(studentDTOsThatHasUserAlready.stream().map(studentDTO ->
                studentDTO.getUserId()).collect(Collectors.toList()));
        return result;
    }
}
