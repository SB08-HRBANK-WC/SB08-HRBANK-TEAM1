package com.wc.hr_bank.dto.request.department;

import java.time.LocalDate;

public record DepartmentRequest
        (
                String name,
                String description,
                LocalDate establishedDate
        )
{}
