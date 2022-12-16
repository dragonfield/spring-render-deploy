package com.example.common.exception;

import java.util.List;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
public class ValidationException extends RuntimeException {

  private final List<FieldError> errors;

  public ValidationException(List<FieldError> errors) {
    this.errors = errors;
  }

}
