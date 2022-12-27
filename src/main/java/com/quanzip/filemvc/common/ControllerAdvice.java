package com.quanzip.filemvc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;
import java.util.Objects;

@org.springframework.web.bind.annotation.ControllerAdvice(
        basePackages = {"com/quanzip/filemvc/controller"})
public class ControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView catchException(Exception ex){
        logger.error("ex: ", ex);
        String msg = ex.getMessage().isEmpty() ? ex.getLocalizedMessage() : ex.getMessage();
        logger.error(msg);
        ex.printStackTrace();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("msg", msg);


        String trace = "";
        for(StackTraceElement stackTraceElement: ex.getStackTrace()){
            trace += ("\n at " + stackTraceElement.toString());
        }
        modelAndView.addObject("trace", trace);

        Throwable cause = ex.getCause();
        String causeMessage;
        if(!Objects.isNull(cause) && (causeMessage = cause.getMessage())!= null){
            modelAndView.addObject("cause", causeMessage);
        }
        modelAndView.setViewName("/error/error");
        return modelAndView;
    }

    @ExceptionHandler(value = NoResultException.class)
    public String catchNoResultException(NoResultException noResultException){
        logger.error("ex: ", noResultException);
        return "/error/noresult";
    }
}
