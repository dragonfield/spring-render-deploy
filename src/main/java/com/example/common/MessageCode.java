package com.example.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MessageCode {

  UNEXPECTED_ERROR("0000", "unexpected exception is occurred. [%s]"),
  VALIDATION_ERROR("0001", "request validation error is occurred. [%s]"),
  EMPLOYEE_NOT_FOUND("0002", "specified employee [id = %s] is not found.");

  private final String code;

  private final String message;

}
