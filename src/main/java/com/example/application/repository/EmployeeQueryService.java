package com.example.application.repository;

import com.example.domain.model.Employee;
import java.util.List;

public interface EmployeeQueryService {
  List<Employee> findAll();

}
