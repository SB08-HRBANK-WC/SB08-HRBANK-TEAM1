package com.wc.hr_bank.dto.request.employee;

import com.wc.hr_bank.entity.EmployeeStatus;
import java.time.LocalDate;

/**
 * 직원 정보 수정을 위한 데이터 record입니다.
 */
public record EmployeeUpdateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    LocalDate hireDate,
    EmployeeStatus status,
    String memo
) {}