package com.wc.hr_bank.dto.response.employee;

public record EmployeeDistDto(
    String groupKey,
    Long count,
    Double percentage
) {
}
