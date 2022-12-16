package com.example.application.service;

import com.example.application.repository.EmployeeRepository;
import com.example.domain.model.EmployeeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteEmployeeUseCase {

  private final EmployeeRepository repository;

  @Transactional
  public void handle(String id) {
    repository.delete(new EmployeeId(id));
  }

}
