package com.wc.hr_bank.dto.response;

import java.time.OffsetDateTime;

public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String message,
        String details
) {}
