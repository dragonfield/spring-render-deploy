package com.example.application.service;

import com.example.application.repository.EmployeeRepository;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateEmployeeUseCase {

  private final EmployeeRepository repository;

  @Transactional
  public void handle(UpdateEmployeeCommand command) {
    repository.update(new Employee(new EmployeeId(command.getId()),
                                   command.getFirstName(),
                                   command.getLastName()));
  }

}
