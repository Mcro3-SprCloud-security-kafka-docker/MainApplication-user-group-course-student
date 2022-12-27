package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.common.DateTimeUtils;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.RoleService;
import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.RoleDTO;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/role")
public class RoleController {
    private final String CLASS = "ROLE";
    private final String VIEW_SEARCH = "/role/search";
    private final String VIEW_ADD = "/role/add";
    private final String VIEW_REDIRECT = "redirect:/role/search";
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping(value = "/search")
    public ModelAndView searchAndGet(@RequestParam(name = "search", required = false) String search
            , Pageable pageable, ModelAndView modelAndView) {
        List<RoleDTO> roleDTOList = roleService.searchAndGetRole(search, pageable);

        modelAndView.addObject("roles", roleDTOList);
        modelAndView.setViewName(VIEW_SEARCH);
        logger.info("Searching for Roles, size: " + roleDTOList.size());

        kafkaService.sendStatisticByKafka("P3: Search roles at " + DateTimeUtils.getDateNowToString(), CLASS);
        return modelAndView;

    }

    @GetMapping(value = "/delete/{roleId}")
    public String deleteByRoleId(@PathVariable(value = "roleId") Long roleId) throws Exception {
        roleService.deleleRoleAllUserByRoleId(roleId);
        logger.info("Deleted role by id: " + roleId);
        kafkaService.sendStatisticByKafka("P3: Deleted role id: " + roleId + " at " + DateTimeUtils.getDateNowToString(), CLASS);

        // calling email service
        kafkaService.sendMail("Deleted role by Id: " + roleId + " at " + DateTimeUtils.getDateNowToString());
        return VIEW_REDIRECT;
    }

    @GetMapping(value = "form-add-role")
    public ModelAndView getFormAdd(ModelAndView modelAndView) {
        List<UserDTO> userDTOList = userService.searchByName("");

        modelAndView.addObject("users", userDTOList);
        modelAndView.setViewName(VIEW_ADD);
        return modelAndView;
    }

    @PostMapping(value = "/add")
    public String addRole(@ModelAttribute RoleDTO roleDTO) throws Exception {
        roleService.addRole(roleDTO);
        logger.info("Added role :" + roleDTO.getRole());
        kafkaService.sendStatisticByService("P3: Added role name: " + roleDTO.getRole() + " at " + DateTimeUtils.getDateNowToString(), CLASS);

        return VIEW_REDIRECT;
    }
}
