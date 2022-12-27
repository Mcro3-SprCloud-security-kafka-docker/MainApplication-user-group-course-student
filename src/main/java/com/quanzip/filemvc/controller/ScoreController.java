package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.client.StatisticService;
import com.quanzip.filemvc.common.DateTimeUtils;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.ScoreService;
import com.quanzip.filemvc.service.dto.ScoreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/score")
public class ScoreController {
    private final String CLASS = "SCORE";
    private final String REDIRECT_STUDENT_VIEW_DETAIL = "redirect:/student/view-course-detail";

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private KafkaService kafkaService;

    @PostMapping(value = "/update")
    public String updateScore(@ModelAttribute ScoreDTO scoreDTO) throws Exception {
        scoreService.saveScore(scoreDTO);

        kafkaService.sendStatisticByKafka("P3: Updated score of course:"+ scoreDTO.getCourseName() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT_STUDENT_VIEW_DETAIL + "?id=" + scoreDTO.getStudentId();
    }
}
