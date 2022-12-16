package com.example.application.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Getter
public class CreateEmployeeCommand {

  private final String firstName;

  private final String lastName;

}
