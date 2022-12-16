package com.example.infrastructure.repository;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.example.application.repository.EmployeeRepository;
import com.example.common.exception.EmployeeNotFoundException;
import com.example.common.exception.SystemException;
import com.example.domain.model.Employee;
import com.example.domain.model.EmployeeId;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.mapper.EmployeeMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

  private final EmployeeMapper mapper;

  @Override
  public Optional<Employee> findById(EmployeeId id) {
    try {
      EmployeeEntity entity = mapper.find(id.getValue());

      if (nonNull(entity)) {
        return Optional.of(entity.toModel());
      } else {
        return Optional.empty();
      }
    } catch (DataAccessException e) {
      throw new SystemException(e);
    }
  }

  @Override
  public Employee create(Employee employee) {
    try {
      Long employeeId = mapper.numberEmployeeId();

      EmployeeEntity entity
              = new EmployeeEntity(Long.toString(employeeId),
                                   employee.getFirstName(),
                                   employee.getLastName());
      mapper.create(entity);

      return new Employee(Long.toString(employeeId),
                          employee.getFirstName(),
                          employee.getLastName());
    } catch (DataAccessException e) {
      throw new SystemException(e);
    }
  }

  @Override
  public void update(Employee employee) {
    try {
      EmployeeEntity entity = mapper.find(employee.getId().getValue());

      if (isNull(entity)) {
        throw new EmployeeNotFoundException(employee.getId().getValue());
      }

      EmployeeEntity toUpdate = entity.merge(employee);
      mapper.update(toUpdate);

    } catch (DataAccessException e) {
      throw new SystemException(e);
    }
  }

  @Override
  public void delete(EmployeeId id) {
    try {
      EmployeeEntity entity = mapper.find(id.getValue());

      if (isNull(entity)) {
        throw new EmployeeNotFoundException(id.getValue());
      }

      mapper.delete(id.getValue());

    } catch (DataAccessException e) {
      throw new SystemException(e);
    }
  }

}
