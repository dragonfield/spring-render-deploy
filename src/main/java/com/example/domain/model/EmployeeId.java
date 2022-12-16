package com.example.domain.model;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNumeric;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class EmployeeId {

  private final String value;

  protected EmployeeId() {
    this.value = "";
  }

  public EmployeeId(String value) {
    if (isNull(value)) {
      throw new IllegalArgumentException("employee id is null. (value=" + value + ")");
    }

    if (!isNumeric(value)) {
      throw new IllegalArgumentException("employee id is not number. (value=" + value + ")");
    }

    if (value.length() > 10) {
      throw new IllegalArgumentException("length of employee id is over 10. (length=" + value.length() + ")");
    }

    this.value = value;
  }

  public static class NullEmployeeId extends EmployeeId {

  }

}
