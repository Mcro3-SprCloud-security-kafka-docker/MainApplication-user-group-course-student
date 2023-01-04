package com.quanzip.filemvc.service.schedule;

import com.quanzip.filemvc.entity.BirthDay;
import com.quanzip.filemvc.entity.Statistic;
import com.quanzip.filemvc.repository.BirthDayRepository;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.UserService;
import com.quanzip.filemvc.service.dto.StatisticDTO;
import com.quanzip.filemvc.service.dto.UserDTO;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    @Autowired
    KafkaService kafkaService;

    @Autowired
    private UserService userService;

    @Autowired
    private BirthDayRepository birthDayRepository;

    @Value(value = "${logging.file.name}")
    private String logPath;

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
            int result = 1; // kafkaService.sendMail(message);
            birthDay.setMailSent(result == 1);
            return birthDay;
        }).collect(Collectors.toList());
        birthDayRepository.saveAll(birthDays);
    }

    @Scheduled(cron = "0/15 * * * * *")
    @SchedulerLock(name = "LogsDeleter", lockAtLeastFor = 10000, lockAtMostFor = 12000)
    public void deleteLogs(){
        final String clazz = "";
        // get all file names in log folder
        final String logPath = "./" + this.logPath.substring(0, this.logPath.indexOf("/"));
        File logFolder = getLogsFolder(logPath);
        List<String> fileNames = Arrays.asList(Objects.requireNonNull(logFolder.list()));

        int count = 0;
        if(!fileNames.isEmpty()){
            // sort to be decrease in file names
            String currentYear = Calendar.getInstance().get(Calendar.YEAR) + "";
            String previousYear = (Integer.parseInt(currentYear) - 1) + "";
            Comparator<String> stringComparator = (s, s1) -> s1.compareToIgnoreCase(s);
//            keep the current file log and other file which is not the legacy log file like (.git, .DSstore,...), not to delete
            fileNames = fileNames.stream().filter(name -> name.contains(currentYear) || name.contains(previousYear))
                    .sorted(stringComparator)
                    .collect(Collectors.toList());
//            keep the nearest log file, not to delete
            fileNames.remove(0);

            for(String fileName: fileNames){
                File logFile = new File(logFolder, fileName);
                if(!logFile.exists()) continue;
                logFile.delete();
                count++;
            }
        }

        // testing schedulerLock
        String message="Main app: Deleted logs ... size: " + count;
        logger.info(message);
        kafkaService.sendStatisticByKafka(message, clazz);

    }

    private File getLogsFolder(String path){
        File file = new File(path);
        if(!file.exists()) {
            logger.info("Creating log folder: "+ file.getParentFile() + " result: " + file.mkdir());
        }
        return  file;
    }
}
