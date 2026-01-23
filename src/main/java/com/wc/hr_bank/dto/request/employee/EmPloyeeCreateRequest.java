package com.wc.hr_bank.dto.request.file;

import com.wc.hr_bank.entity.EmployeeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class EmployeeCreateRequest
{

  @NotBlank(message = "이름을 입력해주세요.")
  private String name;

  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @NotNull(message = "부서를 선택해주세요.")
  private Long departmentId;

  @NotBlank(message = "직함을 입력해주세요.")
  private String position;

  @NotNull(message = "입사일을 선택해주세요.")
  private LocalDate hireDate;

  private String memo;

}