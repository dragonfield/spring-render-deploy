package com.example.infrastructure.repository;

import com.example.application.repository.EmployeeQueryService;
import com.example.common.exception.SystemException;
import com.example.domain.model.Employee;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.mapper.EmployeeMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EmployeeQueryServiceImpl implements EmployeeQueryService {

  private final EmployeeMapper mapper;

  @Override
  public List<Employee> findAll() {
    try {
      List<EmployeeEntity> employeeEntities = mapper.findAll();
      return employeeEntities.stream().map(EmployeeEntity::toModel).toList();

    } catch (DataAccessException e) {
      throw new SystemException(e);
    }
  }

}
