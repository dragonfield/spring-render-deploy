package com.example.presentation;

import com.example.application.service.CreateEmployeeCommand;
import com.example.application.service.CreateEmployeeUseCase;
import com.example.application.service.DeleteEmployeeUseCase;
import com.example.application.service.FindEmployeeUseCase;
import com.example.application.service.ListAllEmployeesUseCase;
import com.example.application.service.UpdateEmployeeCommand;
import com.example.application.service.UpdateEmployeeUseCase;
import com.example.common.exception.ValidationException;
import com.example.domain.model.Employee;
import com.example.presentation.payload.CreateEmployeeRequest;
import com.example.presentation.payload.GetEmployeeResponse;
import com.example.presentation.payload.ListEmployeesResponse;
import com.example.presentation.payload.UpdateEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/employees")
public class EmployeeControllerV1 {

  private final FindEmployeeUseCase findEmployeeUseCase;
  private final ListAllEmployeesUseCase listAllEmployeesUseCase;
  private final CreateEmployeeUseCase createEmployeeUseCase;
  private final UpdateEmployeeUseCase updateEmployeeUseCase;
  private final DeleteEmployeeUseCase deleteEmployeeUseCase;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ListEmployeesResponse listAllEmployees() {
    return ListEmployeesResponse.of(listAllEmployeesUseCase.handle());
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public GetEmployeeResponse getEmployee(@PathVariable String id) {
    Employee employee = findEmployeeUseCase.handle(id);
    return GetEmployeeResponse.of(employee);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<Void> createEmployee(@RequestBody @Validated CreateEmployeeRequest request, BindingResult result) {
    if (result.hasErrors()) {
      throw new ValidationException(result.getFieldErrors());
    }

    Employee employee
            = createEmployeeUseCase.handle(new CreateEmployeeCommand(request.firstName(),
                                                                     request.lastName()));

    String currentUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(UriComponentsBuilder.fromUriString(currentUri)
                                            .path("/" + employee.getId().getValue())
                                            .build()
                                            .toUri());

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateEmployee(@PathVariable String id,
                             @RequestBody @Validated UpdateEmployeeRequest request,
                             BindingResult result) {
    if (result.hasErrors()) {
      throw new ValidationException(result.getFieldErrors());
    }

    UpdateEmployeeCommand command = new UpdateEmployeeCommand(id);
    command.setFirstName(request.firstName());
    command.setLastName(request.lastName());
    updateEmployeeUseCase.handle(command);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteEmployee(@PathVariable String id) {
    deleteEmployeeUseCase.handle(id);
  }

}
