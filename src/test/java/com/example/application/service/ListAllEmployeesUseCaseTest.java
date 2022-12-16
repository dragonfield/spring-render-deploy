package com.example.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.application.repository.EmployeeQueryService;
import com.example.domain.model.Employee;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ListAllEmployeesUseCaseTest {

  @InjectMocks
  ListAllEmployeesUseCase target;

  @Mock
  EmployeeQueryService queryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_正常にすべての従業員のモデルが取得できる場合() {
    // setup
    when(queryService.findAll())
            .thenReturn(List.of(
                    new Employee("1", "Taro", "Yamada"),
                    new Employee("2", "Jiro", "Yamada")
            ));

    // execute
    List<Employee> actual = target.handle();

    // assert
    List<Employee> expected
            = List.of(
                    new Employee("1", "Taro", "Yamada"),
                    new Employee("2", "Jiro", "Yamada")
            );
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

}