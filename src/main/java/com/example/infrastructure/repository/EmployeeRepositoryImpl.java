package com.example.infrastructure.repository;

import static java.util.Objects.nonNull;

import com.example.application.repository.EmployeeRepository;
import com.example.common.SystemException;
import com.example.domain.model.Employee;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    @Autowired
    EmployeeMapper mapper;

    @Override
    public Optional<Employee> find(String id) {
        Employee result = null;

        try {
            EmployeeEntity entity = mapper.find(id);

            if (nonNull(entity)) {
                result = entity.toModel();
            }
        } catch (DataAccessException e) {
            throw new SystemException(e);
        }

        return Optional.ofNullable(result);
    }

}
