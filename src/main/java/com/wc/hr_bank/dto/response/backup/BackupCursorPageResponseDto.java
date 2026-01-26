package com.wc.hr_bank.dto.response.backup;

import java.util.List;

public record BackupCursorPageResponseDto(
        List<BackupDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        Long totalElement,
        boolean hasNext
) {}
