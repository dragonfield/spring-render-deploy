package com.example.infrastructure.repository;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.application.repository.EmployeeQueryService;
import com.example.application.repository.EmployeeRepository;
import com.example.common.exception.EmployeeNotFoundException;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import java.util.List;
import java.util.Optional;
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
class EmployeeRepositoryImplTest {

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
  EmployeeRepository target;

  @Autowired
  EmployeeQueryService queryService;

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
  void test_正常に指定したIDで従業員が取得できる場合() {
    // setup

    // execute
    Optional<Employee> actual = target.findById(new EmployeeId("101"));

    // assert
    Optional<Employee> expected = Optional.of(new Employee("101", "Taro", "Yamada"));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_指定したIDで従業員が取得できない場合() {
    // setup

    // execute
    Optional<Employee> actual = target.findById(new EmployeeId("9999"));

    // assert
    Optional<Employee> expected = Optional.empty();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_正常に従業員情報が登録できる場合() {
    // setup

    // execute
    Employee actual = target.create(new Employee(new EmployeeId.NullEmployeeId(), "Hanako", "Yamada"));

    // assert
    assertThat(actual.getId().getValue()).isNotBlank();
    assertThat(actual.getFirstName()).isEqualTo("Hanako");
    assertThat(actual.getLastName()).isEqualTo("Yamada");
  }

  @Test
  void test_正常に従業員情報が更新できる場合() {
    // setup

    // execute
    target.update(new Employee(new EmployeeId("101"), "Taro1", "Yamada1"));

    // assert
    Optional<Employee> actual = target.findById(new EmployeeId("101"));
    Optional<Employee> expected = Optional.of(new Employee(new EmployeeId("101"), "Taro1", "Yamada1"));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_対象の従業員情報が存在せず更新できない場合() {
    // setup

    // execute
    Employee employee = new Employee(new EmployeeId("9999"), "Taro1", "Yamada1");
    EmployeeNotFoundException actual
            = assertThrows(EmployeeNotFoundException.class, () -> target.update(employee));

    // assert
    assertThat(actual.getId()).isEqualTo("9999");
  }

  @Test
  void test_正常に指定したIDの従業員情報が削除できる場合() {
    // setup

    // execute
    target.delete(new EmployeeId("101"));

    // assert
    List<Employee> actual = queryService.findAll();
    List<Employee> expected
            = List.of(
                  new Employee("102", "Jiro", "Yamada"),
                  new Employee("103", "Saburo", "Yamada")
            );
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

  @Test
  void test_指定したIDの従業員情報が存在せず削除できない場合() {
    // setup

    // execute
    EmployeeId employeeId = new EmployeeId("9999");
    EmployeeNotFoundException actual
            = assertThrows(EmployeeNotFoundException.class, () -> target.delete(employeeId));

    // assert
    assertThat(actual.getId()).isEqualTo("9999");
  }

}