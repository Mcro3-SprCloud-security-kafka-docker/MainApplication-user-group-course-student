package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.client.EmailService;
import com.quanzip.filemvc.client.StatisticService;
import com.quanzip.filemvc.common.DateTimeUtils;
import com.quanzip.filemvc.service.*;
import com.quanzip.filemvc.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    private final String CLASS = "STUDENT";
    private final String COURSE_SCORE_FORM = "/student/course-score-form";
    private final String EDIT = "/student/edit";
    private final String EDIT_INFO = "/student/student-edit-score";
    private final String SEARCH = "/student/search";
    private final String ADD = "/student/add";
    private final String INFO = "/student/student-info";
    private final String REDIRECT_SEARCH = "redirect:" + SEARCH;
    private final String REDIRECT_DETAIL = "redirect:" + "/student/view-course-detail";
    private final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private KafkaService kafkaService;

    @GetMapping(value = "/search")
    public ModelAndView searchStudent(@RequestParam(value = "search", required = false) String search
    , ModelAndView modelAndView){
        logger.info("Getting all students...");
        List<StudentDTO> studentDTOS = studentService.getAllStudent(search, "%" + search + "%");
        modelAndView.addObject("students", studentDTOS);
        modelAndView.setViewName(SEARCH);

        // Calling statistic-service
        kafkaService.sendStatisticByKafka("P3: Search students at: "  + DateTimeUtils.getDateNowToString(), CLASS);
        return modelAndView;
    }

    @GetMapping(value = "/add-form")
    public ModelAndView showFormAdd(ModelAndView modelAndView){
        // only get user which do not be a student at the moment
        List<UserDTO> userDTOS = userService.findUserNotStudent();
        modelAndView.addObject("users", userDTOS);
        modelAndView.setViewName(ADD);
        return modelAndView;
    }

    @PostMapping(value = "/add")
    public ModelAndView addStudent(@ModelAttribute StudentDTO studentDTO, ModelAndView modelAndView) throws Exception {
        studentService.addStudent(studentDTO);
        modelAndView.setViewName(REDIRECT_SEARCH);
        kafkaService.sendStatisticByKafka("P3: Added student code: "+ studentDTO.getCode() + " at " + DateTimeUtils.getDateNowToString(), CLASS);

        return modelAndView;
    }

    @GetMapping(value = "/edit-from-student-only")
    public ModelAndView editStudent(@RequestParam(value = "id") Long id, ModelAndView modelAndView) throws Exception {
        StudentDTO studentDTO = studentService.findStudentById(id);
        modelAndView.addObject("student",studentDTO);
        modelAndView.setViewName(EDIT);
        return modelAndView;
    }

    @PostMapping(value = "/edit")
    public String updateStudent(@ModelAttribute StudentDTO studentDTO) throws Exception {
        studentService.updateStudent(studentDTO);

        kafkaService.sendStatisticByKafka("P3: Editted student code: "+ studentDTO.getCode() + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT_SEARCH;
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteStudent(@PathVariable(value = "id", required = false) Long id) throws Exception {
        studentService.deleteUserById(id);

        kafkaService.sendStatisticByService("P3: Deleted student id: "+ id + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT_SEARCH;
    }

    @GetMapping(value = "/view-course-detail")
    public ModelAndView getStudentInfo(@RequestParam(value = "id", required = false) Long id,
                                       @RequestParam(value = "courseName", required = false) String courseName,
                                       ModelAndView modelAndView) throws Exception {
        StudentDTO studentDTO = studentService.findStudentById(id);
//        Inside student DTO contains scoreList, in each score contains course's name already, so that we dont
//        have to query for extra things like scores or courses by the way.
        List<ScoreDTO> scoreDTOS;
        // case search for course naFme
        if(!Objects.isNull(courseName) && !courseName.isEmpty()){
            scoreDTOS = studentDTO.getScoreDTOS().stream().filter(
                    scoreDTO -> scoreDTO.getCourseName().toLowerCase(Locale.ROOT).contains(courseName.toLowerCase(Locale.ROOT)))
                    .sorted((o1, o2) -> o1.getCourseName().compareToIgnoreCase(o2.getCourseName()))
                    .collect(Collectors.toList());
        }else {
            scoreDTOS = studentDTO.getScoreDTOS().stream()
                    .sorted((o1, o2) -> o1.getCourseName().compareToIgnoreCase(o2.getCourseName()))
                    .collect(Collectors.toList());
        }
        studentDTO.setScoreDTOS(scoreDTOS);

        if(studentDTO.getScoreDTOS().size() > 0){
            // max score smong course
            ScoreDTO maxScore = studentDTO.getScoreDTOS().stream().max((o1, o2) -> o1.getScore().compareTo(o2.getScore())).get();
            // min score among course
            ScoreDTO minScore = studentDTO.getScoreDTOS().stream().max((o1, o2) -> o2.getScore().compareTo(o1.getScore())).get();
            modelAndView.addObject("maxScore", maxScore);
            modelAndView.addObject("minScore", minScore);
        }
        modelAndView.addObject("student", studentDTO);
        modelAndView.setViewName(INFO);
        return modelAndView;
    }

    @GetMapping(value = "/add-course-score-form/{studentId}")
    public ModelAndView addScoreToCourse(@PathVariable(value = "studentId") Long id, ModelAndView modelAndView) throws Exception {
        StudentDTO studentDTO = studentService.findStudentById(id);
        List<CourseDTO> courses = courseService.findAllCourse(null, null);

        modelAndView.addObject("courses", courses);
        modelAndView.addObject("student", studentDTO);
        modelAndView.setViewName(COURSE_SCORE_FORM);
        return modelAndView;
    }

    @PostMapping(value = "/course-score-add")
    public String addCourseScore(@ModelAttribute ScoreDTO scoreDTO) throws Exception {
        scoreService.saveScore(scoreDTO);
        return REDIRECT_DETAIL + "?id=" + scoreDTO.getStudentId();
    }

    @GetMapping(value = "/edit-score")
    public ModelAndView editScoreOfStudent(@RequestParam(value = "scoreId", required = false) Long scoreId,
                                           @RequestParam(value = "studentId", required = false) Long studentId,
                                           ModelAndView modelAndView) throws Exception {
        StudentDTO result = studentService.findStudentById(studentId);
        // filter to get only score editting
        List<ScoreDTO> singleScore = result.getScoreDTOS().stream().filter(scoreDTO -> scoreDTO.getId().equals(scoreId))
                .collect(Collectors.toList());
        result.setScoreDTOS(singleScore);
        modelAndView.addObject("student", result);
        modelAndView.setViewName(EDIT_INFO);
        return modelAndView;
    }

    @GetMapping(value = "/delete-score/{scoreId}/{studentId}")
    public String deleteScoreOfStudent(@PathVariable(value = "scoreId", required = false) Long scoreId,
                                       @PathVariable(value = "studentId", required = false) Long studentId) throws Exception {
        scoreService.deleteByScoreId(scoreId);

        // calling email service
        kafkaService.sendMail("Delete student : " + studentId + " at " + DateTimeUtils.getDateNowToString());
        kafkaService.sendStatisticByService("P3: Delete student by studentId: "+ studentId + " at " + DateTimeUtils.getDateNowToString(), CLASS);
        return REDIRECT_DETAIL + "?id=" + studentId;
    }
}
