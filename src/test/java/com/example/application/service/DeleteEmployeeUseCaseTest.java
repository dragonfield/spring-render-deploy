package com.example.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.application.repository.EmployeeRepository;
import com.example.common.exception.EmployeeNotFoundException;
import com.example.domain.model.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DeleteEmployeeUseCaseTest {

  @InjectMocks
  DeleteEmployeeUseCase target;

  @Mock
  EmployeeRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_正常に従業員情報を削除できる場合() {
    // setup

    // execute
    target.handle("101");

    // assert
    verify(repository, times(1)).delete(new EmployeeId("101"));
  }

  @Test
  void test_対象の従業員情報が存在せず削除できない場合() {
    // setup
    doThrow(new EmployeeNotFoundException("101"))
            .when(repository)
            .delete(new EmployeeId("101"));

    // execute
    EmployeeNotFoundException actual
            = assertThrows(EmployeeNotFoundException.class, () -> target.handle("101"));

    // assert
    assertThat(actual.getId()).isEqualTo("101");
  }

}