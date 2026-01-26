package com.wc.hr_bank.dto.response.changelog;

import java.util.List;

public record CursorPageResponseChangeLogDto
    (
        List<ChangeLogDto> content,
        String nextCursor,
        Long nextIdAfter,
        Integer size,
        Long totalElements,
        Boolean hasNext
    )
{
}
