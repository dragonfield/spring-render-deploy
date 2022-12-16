package com.example.presentation.payload;

import com.example.domain.model.Employee;
import com.fasterxml.jackson.annotation.JsonProperty;

public record GetEmployeeResponse(
    @JsonProperty("id")
    String id,
    @JsonProperty("firstName")
    String firstName,
    @JsonProperty("lastName")
    String lastName
) {
  public static GetEmployeeResponse of(final Employee employee) {
    return new GetEmployeeResponse(employee.getId().getValue(),
                                   employee.getFirstName(),
                                   employee.getLastName());
  }

}
