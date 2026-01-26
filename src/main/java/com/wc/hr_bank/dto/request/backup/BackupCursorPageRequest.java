package com.wc.hr_bank.dto.request.backup;

import com.wc.hr_bank.entity.StatusType;

import java.time.Instant;

public record BackupCursorPageRequest(
        String worker,
        StatusType status,
        Instant startedAtFrom,
        Instant startedAtTo,
        Long idAfter,
        int size,
        String sortField,
        String sortDirection,
        String cursor
) {}
