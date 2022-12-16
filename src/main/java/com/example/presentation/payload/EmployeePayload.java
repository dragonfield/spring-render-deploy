package com.example.presentation.payload;

import com.example.domain.model.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EmployeePayload(
    @JsonProperty("id")
    String id,
    @JsonProperty("firstName")
    String firstName,
    @JsonProperty("lastName")
    String lastName
) {
  public static EmployeePayload of(final Employee employee) {
    return new EmployeePayload(employee.getId().getValue(),
                               employee.getFirstName(),
                               employee.getLastName());
  }
}
