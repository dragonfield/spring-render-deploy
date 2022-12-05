package com.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Employee {

    String id;

    String lastName;

    String firstName;

}
