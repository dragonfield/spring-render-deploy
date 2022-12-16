package com.example.application.service;

import com.example.application.repository.EmployeeRepository;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateEmployeeUseCase {

  private final EmployeeRepository repository;

  @Transactional
  public Employee handle(CreateEmployeeCommand command) {
    return repository.create(new Employee(new EmployeeId.NullEmployeeId(),
                                          command.getFirstName(),
                                          command.getLastName()));
  }

}
