package com.example.application.service;

import com.example.application.repository.EmployeeQueryService;
import com.example.domain.model.Employee;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ListAllEmployeesUseCase {

  private final EmployeeQueryService queryService;

  public List<Employee> handle() {
    return queryService.findAll();
  }

}
