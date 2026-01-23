package com.wc.hr_bank.dto.response.backup;

import com.wc.hr_bank.entity.StatusType;

import java.time.Instant;

public record BackupDto(
        Long id,
        String worker,
        StatusType status,
        Long fileId,
        Instant startedAt,
        Instant endedAt
) {}
