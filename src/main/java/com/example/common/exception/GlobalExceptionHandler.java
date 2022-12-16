package com.example.common.exception;

import static com.example.common.MessageCode.EMPLOYEE_NOT_FOUND;
import static com.example.common.MessageCode.UNEXPECTED_ERROR;
import static com.example.common.MessageCode.VALIDATION_ERROR;

import com.example.common.MessageCode;
import com.example.presentation.payload.ErrorResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidationException(ValidationException e) {
    String message = handleMessage(VALIDATION_ERROR, handleFieldValidationErrors(e.getErrors()));
    log.info(message, e);
    return new ErrorResponse(VALIDATION_ERROR.getCode(), message);
  }

  @ExceptionHandler(EmployeeNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleEmployeeNotFoundException(EmployeeNotFoundException e) {
    String message = handleMessage(EMPLOYEE_NOT_FOUND, e.getId());
    log.warn(message, e);
    return new ErrorResponse(EMPLOYEE_NOT_FOUND.getCode(), message);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleUnexpectedException(Exception e) {
    String message = handleMessage(UNEXPECTED_ERROR, e.getMessage());
    log.error(message, e);
    return new ErrorResponse(UNEXPECTED_ERROR.getCode(), message);
  }

  private String handleMessage(MessageCode messageCode, String arg) {
    return String.format(messageCode.getMessage(), arg);
  }

  private String handleFieldValidationErrors(List<FieldError> errors) {
    return errors.stream()
                 .map(error -> error.getField() + ":" + error.getDefaultMessage())
                 .sorted()
                 .collect(Collectors.joining(", "));
  }

}
