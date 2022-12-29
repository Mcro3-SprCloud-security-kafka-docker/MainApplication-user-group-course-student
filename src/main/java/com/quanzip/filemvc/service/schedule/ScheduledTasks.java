package com.quanzip.filemvc.service.schedule;

import com.quanzip.filemvc.entity.BirthDay;
import com.quanzip.filemvc.repository.BirthDayRepository;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class ScheduledTasks {
    @Autowired
    private UserService userService;

    @Autowired
    private BirthDayRepository birthDayRepository;

    @Autowired
    KafkaService kafkaService;
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(cron = "0 30 8 * * *")
    public void collectPeopleOnTheirBithDate() {
        logger.info("Getting users has birthday within this week, deleting old users...");
        birthDayRepository.deleteAll();
        List<UserDTO> userDTOS = userService.fineUserIsOnTheirBirthDay();
        logger.info("There are " + userDTOS.size() + " people has birthday this week");

        // add to birthday table
        List<BirthDay> birthDays = userDTOS.stream().map(userDTO -> {
            BirthDay birthDay = new BirthDay(null, userDTO.getName(), userDTO.getUserName(), userDTO.getBirthDate(), false);

            String message = "Happy birthday to you, be hard, be confident, be successful";
            int result =  1; // kafkaService.sendMail(message);
            birthDay.setMailSent(result == 1);
            return birthDay;
        }).collect(Collectors.toList());
        birthDayRepository.saveAll(birthDays);
    }
}
