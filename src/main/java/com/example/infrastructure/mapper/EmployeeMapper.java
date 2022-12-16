package com.example.infrastructure.mapper;

import com.example.infrastructure.entity.EmployeeEntity;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper {

  @Select("SELECT nextval('EMPLOYEE_ID_SEQ')")
  @Options(flushCache = Options.FlushCachePolicy.TRUE)
  Long numberEmployeeId();

  @Select("SELECT id, first_name, last_name FROM employee WHERE id = #{id}")
  EmployeeEntity find(String id);

  @Select("SELECT id, first_name, last_name FROM employee")
  List<EmployeeEntity> findAll();

  @Insert("INSERT INTO employee (id, first_name, last_name) VALUES (#{id}, #{firstName}, #{lastName})")
  void create(EmployeeEntity employeeEntity);

  @Update("UPDATE employee SET first_name = #{firstName}, last_name = #{lastName} WHERE id = #{id}")
  int update(EmployeeEntity employeeEntity);

  @Delete("DELETE from employee WHERE id = #{id}")
  int delete(String id);

}
