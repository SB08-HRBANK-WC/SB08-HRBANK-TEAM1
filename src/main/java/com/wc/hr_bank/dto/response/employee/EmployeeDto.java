package com.wc.hr_bank.dto.response.employee;

import com.wc.hr_bank.entity.EmployeeStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto

{
  private Long id;
  private String name;
  private String email;
  private String employeeNumber;
  private Long departmentId;
  private String departmentName;
  private String position;
  private LocalDate hireDate;
  private EmployeeStatus status;
  private Long profileImageId;
}