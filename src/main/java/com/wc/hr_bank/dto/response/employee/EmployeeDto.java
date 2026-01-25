package com.wc.hr_bank.dto.response.employee;

import lombok.*;
import java.time.LocalDate;

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
  private String status;
  private Long profileImageId;
}