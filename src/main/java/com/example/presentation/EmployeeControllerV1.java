package com.example.presentation;

import com.example.application.service.EmployeeService;
import com.example.common.EmployeeNotFoundException;
import com.example.domain.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeControllerV1 {

    @Autowired
    EmployeeService service;

    @GetMapping("/{id}")
    public EmployeeResponse getEmployee(@PathVariable String id) {
        Optional<Employee> employee = service.find(id);

        if (employee.isPresent()) {
            return new EmployeeResponse("0001", "Taro", "Yamada");
        }

        throw new EmployeeNotFoundException(id);
    }

}
