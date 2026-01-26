package com.wc.hr_bank.dto.response.changelog;

import java.util.List;

/**
 *
 * @param content       : 실제 수정 이력 조회 데이터
 * @param nextCursor    : 이전 페이지 마지막 요소
 * @param nextIdAfter   : 이전 페이지 마지막 ID
 * @param size          : 사이즈
 * @param totalElements : 수정 이력 데이터 전체 개수
 * @param hasNext       : 다음 페이지 있는지 판별
 */
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