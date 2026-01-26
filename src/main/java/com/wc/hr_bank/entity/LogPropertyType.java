package com.wc.hr_bank.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogPropertyType
{
  EMPLOYEE_NAME("name"),
  EMPLOYEE_NUMBER("employeeNumber"),
  PROFILE_IMAGE("image"),
  EMAIL("email"),
  JOB_TITLE("position"),
  DEPARTMENT("department"),
  JOINED_AT("hireDate"),
  STATUS("status");

  private final String label;
}
