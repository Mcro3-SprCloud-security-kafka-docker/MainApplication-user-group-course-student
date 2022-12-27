package com.quanzip.filemvc.rest;

import com.quanzip.filemvc.controller.CourseController;
import com.quanzip.filemvc.service.CourseService;
import com.quanzip.filemvc.service.dto.CourseDTO;
import com.quanzip.filemvc.service.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/course1")
public class CourseResourse {
    private final String SEARCH = "/course/search";
    private final String ADD = "/course/add";
    private final String EDIT = "/course/edit";
    private final String REDIRECT = "redirect:" + SEARCH;
    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping(value = "/search")
    public String getAndSearchForCourses(@RequestParam(value = "search", required = false) String search
            , HttpServletRequest httpServletRequest){
        List<CourseDTO> result = courseService.findAllCourse(search, "%" + search + "%");
        httpServletRequest.setAttribute("courses", result);
        return SEARCH;
    }

    @GetMapping(value = "/add-form")
    public String showAddForm(){
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
    public ModelAndView updateCourse(@RequestBody CourseDTO courseDTO, ModelAndView modelAndView) throws Exception {
        courseService.updateCourse(courseDTO);
        modelAndView.setViewName(REDIRECT);
        return modelAndView;
    }

    @PostMapping(value = "/add")
    public ResponseDTO<CourseDTO> addCourse(@RequestBody CourseDTO courseDTO) throws Exception {
        courseService.addCourse(courseDTO);
        ResponseDTO<CourseDTO> responseDTO = new ResponseDTO<>(200, "", courseDTO);
        return responseDTO;
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteCourse(@PathVariable(value = "id") Long id) throws Exception {
        courseService.deleteCourseById(id);
        return REDIRECT;
    }
}
