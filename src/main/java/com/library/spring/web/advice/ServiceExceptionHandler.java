package com.library.spring.web.advice;

import com.library.spring.web.model.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Slf4j
@ControllerAdvice(basePackages = {"com.library.spring.web.controller", "com.library.spring.security.controller"})
public class ServiceExceptionHandler {

    @ExceptionHandler({Exception.class})
    protected Object handleInvalidRequest(Exception exception) {

        log.error("Service Failure", exception);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (exception instanceof AccessDeniedException) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else if(exception instanceof AccountException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseBean responseBean = createResponseBean(exception);
        return new ResponseEntity<>(responseBean, headers, httpStatus);
    }

    private ResponseBean createResponseBean(Exception exception) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setId(UUID.randomUUID().toString());
        responseBean.setMessage(exception.getMessage());
        responseBean.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        return responseBean;
    }
}
