package com.wc.hr_bank.dto.response.department;

import java.time.LocalDate;

public record DepartmentDto
        (
                Long id,
                String name,
                String description,
                LocalDate establishedDate
        )
{}
