package com.example.infrastructure.entity;

import static java.util.Objects.nonNull;

import com.example.domain.model.Employee;

public record EmployeeEntity(
    String id,
    String firstName,
    String lastName
) {
  public Employee toModel() {
    return new Employee(id, firstName, lastName);
  }

  public EmployeeEntity merge(Employee employee) {
    String id = id();
    String firstName = nonNull(employee.getFirstName()) ? employee.getFirstName() : firstName();
    String lastName = nonNull(employee.getLastName()) ? employee.getLastName() : lastName();
    return new EmployeeEntity(id, firstName, lastName);
  }

  public static EmployeeEntity of(Employee employee) {
    return new EmployeeEntity(employee.getId().getValue(), employee.getFirstName(), employee.getLastName());
  }
}
