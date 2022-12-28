package com.quanzip.filemvc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.ControllerAdvice(
        basePackages = {"com.quanzip.filemvc.controller"})
public class ControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);
    private final String error = "error/error";
    private final String not_found = "/error/noresult";

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
        modelAndView.setViewName(error);
        return modelAndView;
    }

    @ExceptionHandler(value = NoResultException.class)
    public String catchNoResultException(NoResultException noResultException){
        logger.error("ex: ", noResultException);
        return not_found;
    }

    // this is for @Validation when FE pass data to BE, object has its conditions on fileds
    // @Valid before property, if data passed do not match with conditions, throwable will be catched here
//    @Valid is apply for only @RequestBody and @modelAttribute
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView catchInvalidArgument(MethodArgumentNotValidException ex, ModelAndView modelAndView){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String defaultMessage = errors.stream().map(objectError -> objectError.getDefaultMessage()).collect(Collectors.joining(","));

        modelAndView.addObject("msg", defaultMessage);
        modelAndView.setViewName(error);
        return modelAndView;
    }
}
