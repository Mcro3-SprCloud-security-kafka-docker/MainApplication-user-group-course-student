package com.quanzip.filemvc.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = "/hello")
    public ModelAndView helloProject(ModelAndView modelAndView){
        modelAndView.addObject("msg", "Hello i am quanzip, i need to lear every day");
        modelAndView.setViewName("hello");
        return modelAndView;
    }
}
