package com.example.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.example.application.repository.EmployeeRepository;
import com.example.common.exception.EmployeeNotFoundException;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FindEmployeeUseCaseTest {

  @InjectMocks
  FindEmployeeUseCase target;

  @Mock
  EmployeeRepository repository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_正常に指定したIDで従業員のモデルが取得できる場合() {
    // setup
    when(repository.findById(new EmployeeId("1")))
            .thenReturn(Optional.of(new Employee("1", "Taro", "Yamada")));

    // execute
    Employee actual = target.handle("1");

    // assert
    Employee expected = new Employee("1", "Taro", "Yamada");
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_指定したIDで従業員のモデルが取得できない場合() {
    // setup
    when(repository.findById(new EmployeeId("1"))).thenReturn(Optional.empty());

    // execute
    EmployeeNotFoundException actual
            = assertThrows(EmployeeNotFoundException.class, () -> target.handle("1"));

    // assert
    assertThat(actual.getId()).isEqualTo("1");
  }

}
