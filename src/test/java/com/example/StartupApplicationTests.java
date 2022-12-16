package com.example;

import static org.assertj.core.api.Assertions.*;

import com.example.presentation.EmployeeControllerV1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StartupApplicationTests {

  @Autowired
  EmployeeControllerV1 target;

  @Test
  void contextLoads() {
    assertThat(target).isNotNull();
  }

}
