package com.library.spring.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ModelAndView applicationError(Exception e) {
        ModelAndView error = new ModelAndView("error");
        error.addObject("errorObj", e);
        return error;
    }
}
