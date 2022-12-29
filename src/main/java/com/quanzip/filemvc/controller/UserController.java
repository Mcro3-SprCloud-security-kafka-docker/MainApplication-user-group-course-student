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
//    @Valid will validate data of userDTO passed must match with conditions of properties inside userDTO
    public String addUser(@ModelAttribute @Valid UserDTO userDTO) throws Exception {
        userService.addUser(userDTO);

        // Calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Added user name: "+ userDTO.getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return "redirect:/user/search";
    }

    @GetMapping(value = "/search")
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
    public String updateUser(@ModelAttribute UserDTO userDTO) throws Exception {
        userService.updateUser(userDTO);

        // calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Updated user: " + userDTO.getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        // calling email service
        kafkaService.sendMail("Updated User: " + userDTO.getName() + " at " + DateTimeUtils.getDateNowToString());
        return "redirect:/user/search";
    }


    @GetMapping(value = "form-edit-role/{userId}")
    public ModelAndView showFormToAddRole(@PathVariable(value = "userId") Long userId,
                                          ModelAndView modelAndView) throws Exception {
        UserDTO userDTO = userService.findUserById(userId);
        modelAndView.addObject("user", userDTO);
        modelAndView.setViewName("/user/edit_role");
        return modelAndView;
    }

    @PostMapping(value = "/edit-role")
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
