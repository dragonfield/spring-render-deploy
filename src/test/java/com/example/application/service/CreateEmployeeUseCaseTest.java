package com.example.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.application.repository.EmployeeRepository;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateEmployeeUseCaseTest {

  @InjectMocks
  CreateEmployeeUseCase target;

  @Mock
  EmployeeRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_正常に従業員情報が登録できる場合() {
    // setup
    when(repository.create(new Employee(new EmployeeId.NullEmployeeId(), "Hanako", "Yamada")))
            .thenReturn(new Employee("10", "Hanako", "Yamada"));

    // execute
    Employee actual = target.handle(new CreateEmployeeCommand("Hanako", "Yamada"));

    // assert
    Employee expected = new Employee("10", "Hanako", "Yamada");
    assertThat(actual).isEqualTo(expected);
  }

}