package com.example.application.service;

import com.example.application.repository.EmployeeRepository;
import com.example.domain.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository repository;

    public Optional<Employee> find(String id) {
        return repository.find(id);
    }

}
