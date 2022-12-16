package com.example.application.repository;

import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import java.util.Optional;

public interface EmployeeRepository {
  Optional<Employee> findById(EmployeeId id);

  Employee create(Employee employee);

  void update(Employee employee);

  void delete(EmployeeId id);

}
