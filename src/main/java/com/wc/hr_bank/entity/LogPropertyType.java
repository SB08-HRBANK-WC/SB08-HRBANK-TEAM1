package com.wc.hr_bank.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogPropertyType
{
  EMPLOYEE_NAME("name"),
  EMAIL("email"),
  JOB_TITLE("position"),
  PROFILE_IMAGE("profileImage"),
  DEPARTMENT("departmentName"),
  JOINED_AT("hireDate"),
  STATUS("status");

  private final String label;
}
