package com.example.application.service;

import com.example.application.repository.EmployeeRepository;
import com.example.common.exception.EmployeeNotFoundException;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FindEmployeeUseCase {

  private final EmployeeRepository repository;

  public Employee handle(String id) {
    return repository.findById(new EmployeeId(id))
            .orElseThrow(() -> new EmployeeNotFoundException(id));
  }

}
