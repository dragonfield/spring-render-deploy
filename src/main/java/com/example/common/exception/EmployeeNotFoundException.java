package com.example.common.exception;

import lombok.Getter;

@Getter
public class EmployeeNotFoundException extends RuntimeException {

  private final String id;

  public EmployeeNotFoundException(String id) {
    super();
    this.id = id;
  }

}
