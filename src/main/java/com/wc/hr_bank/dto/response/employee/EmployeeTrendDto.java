package com.wc.hr_bank.dto.response.employee;

import java.time.LocalDate;

public record EmployeeTrendDto(
    Long count,
    LocalDate date,
    Long change,
    Double changeRate
) {

}
