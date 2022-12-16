package com.example.infrastructure.repository;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.assertj.core.api.Assertions.*;

import com.example.application.repository.EmployeeQueryService;
import com.example.domain.model.Employee;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class EmployeeQueryServiceImplTest {

  private static final Operation DELETE_ALL = deleteAllFrom("employee");
  private static final Operation INSERT_EMPLOYEE_DATA
          = insertInto("employee")
              .columns("id", "first_name", "last_name")
              .values("101", "Taro", "Yamada")
              .values("102", "Jiro", "Yamada")
              .values("103", "Saburo", "Yamada")
              .build();

  @Container
  private static final PostgreSQLContainer<?> postgresqlContainer
          = new PostgreSQLContainer<>(DockerImageName.parse(PostgreSQLContainer.IMAGE).withTag("12.7"))
              .withUsername("user")
              .withPassword("pass")
              .withDatabaseName("sample")
              .withInitScript("schema.sql");

  @DynamicPropertySource
  static void setup(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresqlContainer::getUsername);
    registry.add("spring.datasource.password", postgresqlContainer::getPassword);
  }

  @Autowired
  EmployeeQueryService target;

  @Autowired
  DataSource dataSource;

  @BeforeEach
  public void setUp() throws Exception {
    Destination destination = new DataSourceDestination(dataSource);
    Operation operations = sequenceOf(DELETE_ALL, INSERT_EMPLOYEE_DATA);
    DbSetup dbSetup = new DbSetup(destination, operations);
    dbSetup.launch();
  }

  @Test
  void test_正常にすべての従業員が取得できる場合() {
    // setup

    // execute
    List<Employee> actual = target.findAll();

    // assert
    List<Employee> expected
            = List.of(
                    new Employee("101", "Taro", "Yamada"),
                    new Employee("102", "Jiro", "Yamada"),
                    new Employee("103", "Saburo", "Yamada")
            );
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

}