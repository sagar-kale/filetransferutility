package com.sgr.exception.handlers;


import com.sgr.exception.ErrorResponse;
import com.sgr.exception.ExpectationFailedException;
import com.sgr.exception.ResourceNotFoundException;
import com.sgr.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Map<Class, HttpStatus> exceptionStatusMap;

    static {
        exceptionStatusMap = new HashMap<>();
        exceptionStatusMap.put(ResourceNotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionStatusMap.put(ExpectationFailedException.class, HttpStatus.EXPECTATION_FAILED);
        exceptionStatusMap.put(UnauthorizedException.class, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    ResponseEntity<ErrorResponse> handle(Throwable ex) {
        HttpStatus status = exceptionStatusMap.get(ex.getClass());
        if (status == null)
            status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(status.name(), ex.getMessage());

        return new ResponseEntity<>(errorResponse, status);
    }

}
