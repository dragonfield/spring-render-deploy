package com.example.application.repository;

import com.example.domain.model.Employee;
import java.util.Optional;

public interface EmployeeRepository {

    Optional<Employee> find(String id);

}
