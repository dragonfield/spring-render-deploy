package com.example.infrastructure.mapper;

import static com.ninja_squad.dbsetup.Operations.*;
import static org.assertj.core.api.Assertions.*;

import com.example.infrastructure.entity.EmployeeEntity;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeMapperTest {

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
  EmployeeMapper target;

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
  void test_正常に指定したIDでエンティティが取得できる場合() {
    // setup

    // execute
    EmployeeEntity actual = target.find("101");

    // assert
    EmployeeEntity expected = new EmployeeEntity("101", "Taro", "Yamada");
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_指定したIDでエンティティが取得できない場合() {
    // setup

    // execute
    EmployeeEntity actual = target.find("9999");

    // assert
    assertThat(actual).isNull();
  }

  @Test
  void test_正常に従業員番号が採番できる場合() {
    // setup

    // execute
    Long actual = target.numberEmployeeId();
    Long nextActual = target.numberEmployeeId();

    // execute
    assertThat(actual).isNotNull();
    assertThat(nextActual).isNotNull().isEqualTo(actual + 1);
  }

  @Test
  void test_正常に従業員情報が登録できる場合() {
    // setup

    // execute
    EmployeeEntity employeeEntity = new EmployeeEntity("100", "Hanako", "Yamada");
    target.create(employeeEntity);

    // assert
    EmployeeEntity actual = target.find("100");
    EmployeeEntity expected = new EmployeeEntity("100", "Hanako", "Yamada");
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_正常に従業員情報が更新できる場合() {
    // setup

    // execute
    EmployeeEntity employeeEntity = new EmployeeEntity("101", "Taro1", "Yamada2");
    int actual = target.update(employeeEntity);

    // assert
    assertThat(actual).isEqualTo(1);

    EmployeeEntity actualEmployee = target.find("101");
    EmployeeEntity expectedEmployee = new EmployeeEntity("101", "Taro1", "Yamada2");
    assertThat(actualEmployee).isEqualTo(expectedEmployee);
  }

  @Test
  void test_対象の従業員情報が存在せず更新できない場合() {
    // setup

    // execute
    EmployeeEntity employeeEntity = new EmployeeEntity("9999", "Taro1", "Yamada2");
    int actual = target.update(employeeEntity);

    // assert
    assertThat(actual).isZero();
  }

  @Test
  void test_正常に指定したIDの従業員情報が削除できる場合() {
    // setup

    // execute
    int actual = target.delete("101");

    // assert
    assertThat(actual).isEqualTo(1);

    List<EmployeeEntity> actualEmployees = target.findAll();
    List<EmployeeEntity> expectedEmployees
            = List.of(
                    new EmployeeEntity("102", "Jiro", "Yamada"),
                    new EmployeeEntity("103", "Saburo", "Yamada")
            );
    assertThat(actualEmployees).containsExactlyInAnyOrderElementsOf(expectedEmployees);
  }

  @Test
  void test_指定したIDの従業員情報が存在せず削除できない場合() {
    // setup

    // execute
    int actual = target.delete("9999");

    // assert
    assertThat(actual).isZero();

    List<EmployeeEntity> actualEmployees = target.findAll();
    List<EmployeeEntity> expectedEmployees
            = List.of(
                    new EmployeeEntity("101", "Taro", "Yamada"),
                    new EmployeeEntity("102", "Jiro", "Yamada"),
                    new EmployeeEntity("103", "Saburo", "Yamada")
            );
    assertThat(actualEmployees).containsExactlyInAnyOrderElementsOf(expectedEmployees);
  }

  @Test
  void test_正常にすべてのエンティティが取得できる場合() {
    // setup

    // execute
    List<EmployeeEntity> actual = target.findAll();

    // assert
    List<EmployeeEntity> expected
            = List.of(
                    new EmployeeEntity("101", "Taro", "Yamada"),
                    new EmployeeEntity("102", "Jiro", "Yamada"),
                    new EmployeeEntity("103", "Saburo", "Yamada")
            );
    assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
  }

}