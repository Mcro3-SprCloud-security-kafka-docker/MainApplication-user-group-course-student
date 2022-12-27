package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.common.DateTimeUtils;
import com.quanzip.filemvc.service.GroupService;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.GroupDTO;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/group")
public class GroupController {
    private final String CLASS = "GROUP";
    private final String SEARCH = "/group/search";
    private final String ADD = "/group/add-group";
    private final String EDIT = "/group/edit-group";
    private final String REDIRECT = "redirect:/group/search";
    private final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupService groupService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping(value = "/search")
    public ModelAndView searchAndGetAllGroup(@RequestParam(name = "search", required = false) String search,
                                             ModelAndView modelAndView){
        logger.info("Getting all group in system by name: " + search);
        List<GroupDTO> groupDTOS = groupService.findAllGroups(search);
        modelAndView.addObject("groups", groupDTOS);
        modelAndView.setViewName(SEARCH);

        kafkaService.sendStatisticByKafka("P3: Search all groups: "+ groupDTOS.get(0).getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return modelAndView;
    }

    @GetMapping(value = "/form-add")
    public ModelAndView showFormAdd(ModelAndView modelAndView){
        List<String> groupNames = groupService.findAllGroups(null).stream().map(groupDTO -> groupDTO.getName())
                .collect(Collectors.toList());
        modelAndView.addObject("names", groupNames);
        modelAndView.setViewName(ADD);
        return modelAndView;
    }

    @PostMapping(value = "/add-group")
    public ModelAndView addGroup(@ModelAttribute GroupDTO groupDTO, ModelAndView modelAndView) throws Exception {
        GroupDTO savedGroup = groupService.addGroup(groupDTO);
        modelAndView.addObject("msg", messageSource.getMessage("msg.added", new Object[]{savedGroup.getName()}, Locale.getDefault()));
        modelAndView.setViewName(REDIRECT);

        // Using kafka to sent event to consumer
        kafkaService.sendStatisticByKafka("P3: Added group name: "+ groupDTO.getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return modelAndView;
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView editGroup(@PathVariable(value = "id") Long id, ModelAndView modelAndView) throws Exception {
        logger.info("updating group id: " + id);
        GroupDTO groupDTO = groupService.findGroupById(id);
        List<UserDTO> userDTOSOfGroup = groupService.updateGroup(id);
        // all user in system left that do not contain in userDTOSOfGroup
        List<UserDTO> userInSystem = userService.findUserWhereNotIn(userDTOSOfGroup.stream().map(UserDTO::getId)
                .collect(Collectors.toList()));

        modelAndView.addObject("group", groupDTO);
        modelAndView.addObject("usersOfGroup", userDTOSOfGroup);
        modelAndView.addObject("usersInSystem", userInSystem);

        // Calling statistic-service
        kafkaService.sendStatisticByService("P3: Added group name: "+ groupDTO.getName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);

        modelAndView.setViewName(EDIT);
        return modelAndView;
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteGroup(@PathVariable(value = "id") Long id){
        groupService.deleteGroupById(id);

        kafkaService.sendStatisticByService("P3: Deleted group at " + DateTimeUtils.getDateNowToString(), CLASS);

        // calling email service
        kafkaService.sendMail("Delete groupId : " + id + " at " + DateTimeUtils.getDateNowToString());
        return REDIRECT;
    }


}
