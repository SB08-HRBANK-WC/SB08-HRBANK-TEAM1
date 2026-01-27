package com.wc.hr_bank.dto.request.employee;

import com.wc.hr_bank.entity.EmployeeStatus;
import java.time.LocalDate;

/**
 * 직원 목록 조회를 위한 커서 기반 페이징 및 필터 조건 record입니다.
 */
public record EmployeeListRequest(
    String nameOrEmail,
    String employeeNumber,
    String departmentName,
    String position,
    LocalDate hireDateFrom,
    LocalDate hireDateTo,
    EmployeeStatus status,
    Long idAfter,
    String cursor,
    int size,
    String sortField,
    String sortDirection
) {}