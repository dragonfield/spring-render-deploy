package com.example.common;

import com.example.presentation.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(EmployeeNotFoundException e) {
        log.info("specified employee (id={}) is not found.", e.getId(), e);
        return new ErrorResponse("0001", String.format("specified employee (id=%s) is not found.", e.getId()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(Exception e) {
        log.error("unexpected exception is occurred.", e);
        return new ErrorResponse("0000", "unexpected exception is occurred.");
    }

}
