package com.wc.hr_bank.dto.request.employee;

import com.wc.hr_bank.entity.EmployeeStatus;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest

{
  private String name;

  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  private Long departmentId;

  private String position;

  private LocalDate hireDate;

  private EmployeeStatus status; // ACTIVE, ON_LEAVE, RESIGNED

  private String memo;

}