package com.example.application.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Getter
public class UpdateEmployeeCommand {

  private final String id;

  @Setter
  private String firstName;

  @Setter
  private String lastName;

}
