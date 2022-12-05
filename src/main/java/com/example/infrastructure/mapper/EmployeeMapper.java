package com.example.infrastructure.mapper;

import com.example.infrastructure.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    @Select("SELECT id, first_name, last_name FROM employee WHERE id = #{id}")
    EmployeeEntity find(String id);

}
