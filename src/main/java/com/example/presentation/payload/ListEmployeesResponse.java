package com.example.presentation.payload;

import com.example.domain.model.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ListEmployeesResponse(
    @JsonProperty("employees")
    List<EmployeePayload> employees
) {
  public static ListEmployeesResponse of(final List<Employee> employees) {
    return new ListEmployeesResponse(employees.stream().map(EmployeePayload::of).toList());
  }

}
