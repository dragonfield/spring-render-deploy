package com.example.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class Employee {

  private final EmployeeId id;

  private final String firstName;

  private final String lastName;

  public Employee(String id, String firstName, String lastName) {
    this.id = new EmployeeId(id);
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Employee(EmployeeId id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

}
