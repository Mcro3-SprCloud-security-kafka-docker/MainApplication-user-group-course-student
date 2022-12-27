package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.common.DateTimeUtils;
import com.quanzip.filemvc.service.GroupService;
import com.quanzip.filemvc.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/group-user")
public class GroupUserController {
    private final String CLASS = "USER";
    private final Logger logger = LoggerFactory.getLogger(GroupUserController.class);
    private final String GROUP_INFO= "/group/edit";
    private final String REDIRECT = "redirect:" + GROUP_INFO;

    @Autowired
    private GroupService groupService;

    @Autowired
    private KafkaService kafkaService;

    @PostMapping(value = "/add")
    public String addGroupUser(@RequestParam(value = "groupid") Long groupId,
                               @RequestParam(value = "userid") Long userId) throws Exception {
        logger.info("Adding user to group id: " + groupId);
        groupService.addGroupUser(groupId, userId);

        kafkaService.sendStatisticByKafka("P3: Delete group-user by user id: "+ userId + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT+ "/" + groupId;
    }

    @GetMapping(value = "/delete/{groupId}/{userId}")
    public String deleteGroupUser(@PathVariable(value = "groupId") Long groupId
            , @PathVariable(value = "userId") Long userId) throws Exception {
        groupService.deletebyGroupIdAndUserId(groupId, userId);

        kafkaService.sendStatisticByService("P3: Deleted group-user by user Id and group Id: "+ userId + "-" + groupId + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT+ "/" + groupId;
    }
}
