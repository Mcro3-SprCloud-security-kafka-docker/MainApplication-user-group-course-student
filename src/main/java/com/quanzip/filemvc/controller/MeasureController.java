package com.quanzip.filemvc.controller;

import com.quanzip.filemvc.service.PercentService;
import com.quanzip.filemvc.service.dto.PercentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "measure")
public class MeasureController {
    private final String MEASURE = "/measure/show";

    @Autowired
    private PercentService percentService;

    @GetMapping(value = "/show")
    public ModelAndView showMeasure(ModelAndView modelAndView){
        List<PercentDTO> result = percentService.showMeasure();

        List<PercentDTO> funcClicks = result.stream().filter(percentDTO -> percentDTO.getCategory() == 1).collect(Collectors.toList());
        List<PercentDTO> mailResults = result.stream().filter(percentDTO -> percentDTO.getCategory() == 2).collect(Collectors.toList());

        modelAndView.addObject("percentClicks", funcClicks);
        modelAndView.addObject("emailResults", mailResults);
        modelAndView.setViewName(MEASURE);
        return  modelAndView;
    }
}
