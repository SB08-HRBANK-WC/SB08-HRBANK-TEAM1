package com.wc.hr_bank.dto.response.department;

import java.util.List;

public record DepartmentCursorPageResponse(
    List<DepartmentDto> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    Boolean hasNext
) {}
