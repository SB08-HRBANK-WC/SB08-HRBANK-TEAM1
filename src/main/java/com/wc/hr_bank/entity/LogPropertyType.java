package com.wc.hr_bank.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogPropertyType
{
  EMPLOYEE_NAME("이름"),
  EMAIL("이메일"),
  JOB_TITLE("직함"),
  PROFILE_IMAGE("이미지"),
  DEPARTMENT("부서"),
  JOINED_AT("입사일"),
  STATUS("상태");

  private final String label;
}
