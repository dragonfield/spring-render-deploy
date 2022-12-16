package com.example.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.application.repository.EmployeeRepository;
import com.example.common.exception.EmployeeNotFoundException;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UpdateEmployeeUseCaseTest {

  @InjectMocks
  UpdateEmployeeUseCase target;

  @Mock
  EmployeeRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_正常に従業員情報を更新できる場合() {
    // setup

    // execute
    UpdateEmployeeCommand command = new UpdateEmployeeCommand("101");
    command.setFirstName("Taro1");
    command.setLastName("Yamada1");
    target.handle(command);

    // assert
    verify(repository, times(1)).update(new Employee(new EmployeeId("101"), "Taro1", "Yamada1"));
  }

  @Test
  void test_対象の従業員情報が存在せず更新できない場合() {
    // setup
    doThrow(new EmployeeNotFoundException("101"))
            .when(repository)
            .update(new Employee(new EmployeeId("101"), "Taro1", "Yamada1"));

    // execute
    UpdateEmployeeCommand command = new UpdateEmployeeCommand("101");
    command.setFirstName("Taro1");
    command.setLastName("Yamada1");
    EmployeeNotFoundException actual
            = assertThrows(EmployeeNotFoundException.class, () -> target.handle(command));

    // assert
    assertThat(actual.getId()).isEqualTo("101");
  }

}