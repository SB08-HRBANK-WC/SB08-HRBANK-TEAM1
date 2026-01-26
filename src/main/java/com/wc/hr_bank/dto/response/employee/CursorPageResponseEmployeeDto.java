package com.wc.hr_bank.dto.response.employee;

import java.util.List;

/**
 * 명세서 규격에 맞춘 커서 기반 페이지 응답 DTO입니다.
 */
public record CursorPageResponseEmployeeDto(
    List<EmployeeDto> content,
    String nextCursor,    // 다음 페이지 커서 (예: "eyJpZCI6MjB9")
    Long nextIdAfter,     // 마지막 요소의 ID (예: 20)
    int size,             // 페이지 크기
    long totalElements,   // 총 요소 수 (오프셋 방식 지원용)
    boolean hasNext       // 다음 페이지 여부
) {}