package com.example.presentation;

import static com.example.TestUtils.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.example.application.service.CreateEmployeeCommand;
import com.example.application.service.CreateEmployeeUseCase;
import com.example.application.service.DeleteEmployeeUseCase;
import com.example.application.service.FindEmployeeUseCase;
import com.example.application.service.ListAllEmployeesUseCase;
import com.example.application.service.UpdateEmployeeCommand;
import com.example.application.service.UpdateEmployeeUseCase;
import com.example.common.exception.EmployeeNotFoundException;
import com.example.common.exception.SystemException;
import com.example.domain.model.Employee;
import com.example.presentation.payload.CreateEmployeeRequest;
import com.example.presentation.payload.UpdateEmployeeRequest;
import io.restassured.http.Header;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerV1Test {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  FindEmployeeUseCase findEmployeeUseCase;

  @MockBean
  ListAllEmployeesUseCase listAllEmployeesUseCase;

  @MockBean
  CreateEmployeeUseCase createEmployeeUseCase;

  @MockBean
  UpdateEmployeeUseCase updateEmployeeUseCase;

  @MockBean
  DeleteEmployeeUseCase deleteEmployeeUseCase;

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  void test_正常にIDで指定した従業員情報が取得できる場合() {
    // setup
    when(findEmployeeUseCase.handle("1"))
            .thenReturn(new Employee("1", "Taro", "Yamada"));

    // execute & assert
    given()
      .header(new Header("Accept-Language", "en-US"))
    .when()
      .get("/v1/employees/1")
    .then()
      .status(HttpStatus.OK)
      .body("id", equalTo("1"))
      .body("firstName", equalTo("Taro"))
      .body("lastName", equalTo("Yamada"));
  }

  @Test
  void test_正常にすべての従業員情報が取得できる場合() {
    // setup
    when(listAllEmployeesUseCase.handle())
            .thenReturn(List.of(
                    new Employee("1", "Taro", "Yamada"),
                    new Employee("2", "Jiro", "Yamada")
            ));

    // execute & assert
    given()
      .header(new Header("Accept-Language", "en-US"))
    .when()
      .get("/v1/employees")
    .then()
      .status(HttpStatus.OK)
      .body("employees[0].id", equalTo("1"))
      .body("employees[0].firstName", equalTo("Taro"))
      .body("employees[0].lastName", equalTo("Yamada"))
      .body("employees[1].id", equalTo("2"))
      .body("employees[1].firstName", equalTo("Jiro"))
      .body("employees[1].lastName", equalTo("Yamada"));
  }

  @Test
  void test_IDで指定した従業員情報が取得できない場合() {
    // setup
    when(findEmployeeUseCase.handle("9"))
            .thenThrow(new EmployeeNotFoundException("9"));

    // execute & assert
    given()
      .header(new Header("Accept-Language", "en-US"))
    .when()
      .get("/v1/employees/9")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0002"))
      .body("message", equalTo("specified employee [id = 9] is not found."));
  }

  @Test
  void test_正常に従業員情報が登録できる場合() {
    // setup
    when(createEmployeeUseCase.handle(new CreateEmployeeCommand("Hanako", "Yamada")))
            .thenReturn(new Employee("10", "Hanako", "Yamada"));

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new CreateEmployeeRequest("Hanako", "Yamada")))
    .when()
      .post("/v1/employees")
    .then()
      .status(HttpStatus.CREATED)
      .body(emptyString());
  }

  @Test
  void test_従業員情報登録においてリクエストで必須項目が入力されていない場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new CreateEmployeeRequest(null, "Yamada")))
    .when()
      .post("/v1/employees")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0001"))
      .body("message", equalTo("request validation error is occurred. [firstName:must not be blank]"));
  }

  @Test
  void test_従業員情報登録においてリクエストで空文字が入力されている場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new CreateEmployeeRequest("", "Yamada")))
    .when()
      .post("/v1/employees")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0001"))
      .body("message", equalTo("request validation error is occurred. [firstName:must match \"^[a-zA-Z]+$\", firstName:must not be blank]"));
  }

  @Test
  void test_従業員情報登録においてリクエストで不正な文字が入力されている場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new CreateEmployeeRequest("Taro", "Yamada1")))
    .when()
      .post("/v1/employees")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0001"))
      .body("message", equalTo("request validation error is occurred. [lastName:must match \"^[a-zA-Z]+$\"]"));
  }

  @Test
  void test_従業員情報登録においてリクエストで文字数が越えている場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new CreateEmployeeRequest("Taro", "YamadaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")))
    .when()
      .post("/v1/employees")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0001"))
      .body("message", equalTo("request validation error is occurred. [lastName:length must be between 0 and 100]"));
  }

  @Test
  void test_正常に従業員情報を更新できる場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new UpdateEmployeeRequest(null, "Shirato")))
    .when()
      .patch("/v1/employees/1")
    .then()
      .status(HttpStatus.NO_CONTENT)
      .body(emptyString());

    UpdateEmployeeCommand command = new UpdateEmployeeCommand("1");
    command.setLastName("Shirato");
    verify(updateEmployeeUseCase, times(1)).handle(command);
  }

  @Test
  void test_対象の従業員情報が存在せず更新できない場合() {
    // setup
    UpdateEmployeeCommand command = new UpdateEmployeeCommand("1");
    command.setLastName("Shirato");
    doThrow(new EmployeeNotFoundException("1"))
            .when(updateEmployeeUseCase)
            .handle(command);

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new UpdateEmployeeRequest(null, "Shirato")))
    .when()
      .patch("/v1/employees/1")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0002"))
      .body("message", equalTo("specified employee [id = 1] is not found."));
  }

  @Test
  void test_従業員情報更新においてリクエストで空文字が入力されている場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new UpdateEmployeeRequest("", "Shirato")))
    .when()
      .patch("/v1/employees/1")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0001"))
      .body("message", equalTo("request validation error is occurred. [firstName:must match \"^[a-zA-Z]+$\"]"));
  }

  @Test
  void test_従業員情報更新においてリクエストで不正な文字が入力されている場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new CreateEmployeeRequest(null, "Shirato1")))
    .when()
      .patch("/v1/employees/1")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0001"))
      .body("message", equalTo("request validation error is occurred. [lastName:must match \"^[a-zA-Z]+$\"]"));
  }

  @Test
  void test_従業員情報更新においてリクエストで文字数が越えている場合() {
    // setup

    // execute & assert
    given()
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .header(new Header("Accept-Language", "en-US"))
      .body(marshalToJson(new CreateEmployeeRequest(null, "ShiratoAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")))
    .when()
      .patch("/v1/employees/1")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0001"))
      .body("message", equalTo("request validation error is occurred. [lastName:length must be between 0 and 100]"));
  }

  @Test
  void test_正常に従業員情報を削除できる場合() {
    // setup

    // execute & assert
    given()
      .header(new Header("Accept-Language", "en-US"))
    .when()
      .delete("/v1/employees/1")
    .then()
      .status(HttpStatus.NO_CONTENT)
      .body(emptyString());

    verify(deleteEmployeeUseCase, times(1)).handle("1");
  }

  @Test
  void test_対象の従業員情報が存在せず削除できない場合() {
    // setup
    doThrow(new EmployeeNotFoundException("1"))
            .when(deleteEmployeeUseCase)
            .handle("1");

    // execute & assert
    given()
      .header(new Header("Accept-Language", "en-US"))
    .when()
      .delete("/v1/employees/1")
    .then()
      .status(HttpStatus.BAD_REQUEST)
      .body("code", equalTo("0002"))
      .body("message", equalTo("specified employee [id = 1] is not found."));
  }

  @Test
  void test_システム障害が発生した場合() {
    // setup
    when(findEmployeeUseCase.handle("1"))
            .thenThrow(new SystemException());

    // execute & assert
    given()
      .header(new Header("Accept-Language", "en-US"))
    .when()
      .get("/v1/employees/1")
    .then()
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body("code", equalTo("0000"))
      .body("message", equalTo("unexpected exception is occurred. [null]"));
  }

}