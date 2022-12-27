package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.common.DateTimeUtils;
import com.quanzip.filemvc.service.CourseService;
import com.quanzip.filemvc.service.KafkaService;
import com.quanzip.filemvc.service.dto.CourseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/course")
public class CourseController {
    private final String CLASS = "COURSE";
    private final String SEARCH = "/course/search";
    private final String ADD = "/course/add";
    private final String EDIT = "/course/edit";
    private final String REDIRECT = "redirect:" + SEARCH;
    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping(value = "/search")
    public String getAndSearchForCourses(@RequestParam(value = "search", required = false) String search
            , HttpServletRequest httpServletRequest) {
        List<CourseDTO> result = courseService.findAllCourse(search, "%" + search + "%");
        httpServletRequest.setAttribute("courses", result);

        // Calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Search courses at: " + DateTimeUtils.getDateNowToString(), CLASS);
        return SEARCH;
    }

    @GetMapping(value = "/add-form")
    public String showAddForm() {
        return ADD;
    }

    @GetMapping(value = "/edit-form")
    public ModelAndView editCourse(@RequestParam(value = "id", required = false) Long id,
                                   ModelAndView modelAndView) throws Exception {
        CourseDTO edittingCourse = courseService.findCourseById(id);
        modelAndView.addObject("course", edittingCourse);
        modelAndView.setViewName(EDIT);
        return modelAndView;
    }

    @PostMapping(value = "/edit")
    public ModelAndView updateCourse(@ModelAttribute CourseDTO courseDTO, ModelAndView modelAndView) throws Exception {
        courseService.updateCourse(courseDTO);
        modelAndView.setViewName(REDIRECT);

        // Calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Update course at: " + DateTimeUtils.getDateNowToString(), CLASS);
        return modelAndView;
    }

    @PostMapping(value = "/add")
    public String addCourse(@ModelAttribute CourseDTO courseDTO) throws Exception {
        courseService.addCourse(courseDTO);

        // Calling statistic-service
        kafkaService.sendStatisticByService("P3: Added course at: " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT;
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteCourse(@PathVariable(value = "id") Long id) throws Exception {
        courseService.deleteCourseById(id);

        // calling email service
        kafkaService.sendMail("Deleted userId: " + id + " at " + DateTimeUtils.getDateNowToString());
        kafkaService.sendStatisticByService("P3: Deleted course by id: " + id + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT;
    }
}
