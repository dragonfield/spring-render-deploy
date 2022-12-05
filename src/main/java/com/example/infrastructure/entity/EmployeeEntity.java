package com.example.infrastructure.entity;

import com.example.domain.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

    private String id;

    private String lastName;

    private String firstName;

    public Employee toModel() {
        return new Employee(id, lastName, firstName);
    }

}
