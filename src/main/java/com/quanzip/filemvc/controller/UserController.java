package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.client.EmailService;
import com.quanzip.filemvc.client.StatisticService;
import com.quanzip.filemvc.common.DateTimeUtils;
import com.quanzip.filemvc.service.BirthDayService;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.BirthDayDTO;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private BirthDayService birthDayService;

    @GetMapping(value = "/form/add")
    public String showForm(){
        return "user/add";
    }

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaService kafkaService;

    private final String CLASS = "USER";

    @PostMapping(value = "/add")
    @CacheEvict(cacheNames = "user", allEntries = true)
//    @Valid will validate data of userDTO passed must match with conditions of properties inside userDTO
    public String addUser(@ModelAttribute @Valid UserDTO userDTO) throws Exception {
        userService.addUser(userDTO);

        // Calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Added user name: "+ userDTO.getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return "redirect:/user/search";
    }

    @GetMapping(value = "/search")
    // cacheNames = "user" : in memory, there is a table name: user to save data of users
    @Cacheable(cacheNames = "user")
//    Lan dau se luu vao memory, tu lan thu 2 se doc tu cache, se cai thien toc
//    roc do truy van
//    Khi user update/delete, can phai xoa xache da luu trong DB:
//    using @CacheEvict(cacheName = "user", allEntries = true)
    public ModelAndView searchAndViewListUsers(@RequestParam(value = "search", required = false) String name, ModelAndView modelAndView){
        List<UserDTO> userDTOS = userService.searchByName(name).stream().peek(dto ->
                {
                    String folder = environment.getProperty("application.avatar-folder");
                    File avatar = new File(folder+ "/" + dto.getAvatar());
                    dto.setFullAvatar(avatar.getAbsolutePath());

                }).collect(Collectors.toList());

        modelAndView.addObject("users", userDTOS);
        modelAndView.setViewName("user/search");

//        Calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Getting all user at " + DateTimeUtils.getDateNowToString(), CLASS);
        return modelAndView;
    }

    @GetMapping(value = "download-avatar")
    public void downloadUserAvatar(@RequestParam(value = "avatarName") String name, HttpServletResponse httpServletResponse) throws Exception {
        userService.downloadUserAvatar(name, httpServletResponse);
    }

    @GetMapping(value = "/delete")
//    Need to delete both user and student because user can map with student so, delete cache of student is
//    nessesary
    @Caching(evict = {
//            cacheNames = "user" : in memory, there is a table name: user to save data of users
            @CacheEvict(cacheNames = "user", key = "#id") // when deleting an user by Id
//            spring will delete user has this id in cacheMemory(ram) , not all user tai bang USER
            ,@CacheEvict(cacheNames = "student")
    })
    @CacheEvict(cacheNames = "user", allEntries = true)
    public String deleteUser(@RequestParam(value = "id") Long id) throws Exception {
        userService.deleteUserById(id);

        // calling statistic ser
        kafkaService.sendStatisticByKafka("P3: Deleted user at " + DateTimeUtils.getDateNowToString(), CLASS);
        // calling email service
        kafkaService.sendMail("Deleted userId: " + id + " at " + DateTimeUtils.getDateNowToString());
        return "redirect:/user/search";
    }

    @GetMapping(value = "/edit")
    public ModelAndView editUser(@RequestParam(value = "id") Long id, ModelAndView modelAndView) throws Exception {
        UserDTO userDTO = userService.editUserById(id);
        modelAndView.addObject("user", userDTO);
        modelAndView.setViewName("/user/edit");

        return modelAndView;
    }

    @PostMapping(value = "/update")
    @CacheEvict(cacheNames = "user", allEntries = true)
//    @CachePut(cacheNames = "user", key = "#userDTO.id) when we use this, this function must return
//    an userDTO for cache to update(CachePut) and spring will
//    update the user has Id by value: #userDTO.id in param
    public String updateUser(@ModelAttribute UserDTO userDTO) throws Exception {
        userService.updateUser(userDTO);

        // calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Updated user: " + userDTO.getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        // calling email service
        kafkaService.sendMail("Updated User: " + userDTO.getName() + " at " + DateTimeUtils.getDateNowToString());
        return "redirect:/user/search";
    }

    @GetMapping(value = "form-edit-role/{userId}")
//    if using cache tai cac phuong thuc get user bY Id, thi thong tin cua 1 User se duoc luu vao memory
//    Lan query sau cho user do se nhanh hon, nhung cung can xoa cache cua user do khi no dc update/delete
//    do vay can them @CacheEvict tai cac phuong thuc update/delete userByid.
    public ModelAndView showFormToAddRole(@PathVariable(value = "userId") Long userId,
                                          ModelAndView modelAndView) throws Exception {
        UserDTO userDTO = userService.findUserById(userId);
        modelAndView.addObject("user", userDTO);
        modelAndView.setViewName("/user/edit_role");
        return modelAndView;
    }

    @PostMapping(value = "/edit-role")
    @CacheEvict(allEntries = true, cacheNames = "user")
    public String addRole(@ModelAttribute UserDTO userDTO) throws Exception {
        userService.editRole(userDTO);

        // calling email service
        kafkaService.sendStatisticByKafka("Editted role for user: " + userDTO.getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);

        // calling statistic-service
        kafkaService.sendMail("Editted role for user: " + userDTO.getName() + " at " + DateTimeUtils.getDateNowToString());
        return "redirect:/user/search";
    }

    @GetMapping(value = "/birthday")
    public ModelAndView getUserHasBirthDayinWeek(ModelAndView modelAndView){
        List<BirthDayDTO> birthDayDTOS = birthDayService.getAllUserHasBirthDayThisWeek();

        modelAndView.addObject("birthDayDTOs", birthDayDTOS);
        modelAndView.setViewName("/user/birth-day");
        return  modelAndView;
    }
}
