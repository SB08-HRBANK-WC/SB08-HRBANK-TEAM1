package com.wc.hr_bank.dto.request.changelog;


import static com.wc.hr_bank.service.impl.ChangeLogServiceImpl.KOREA_ZONE;

import com.wc.hr_bank.entity.ChangeType;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 수정이력 조회 및 커서 페이지네이션 관련 DTO
 * @param employeeNumber : 대상 직원 사번
 * @param type           : 이력 유형 (CREATED, UPDATED, DELETED)
 * @param memo           : 메모 내용
 * @param ipAddress      : IP 주소
 * @param atFrom         : 수정 일시(부터)
 * @param atTo           : 수정 일시(까지)
 * @param idAfter        : 이전 페이지 마지막 요소 ID
 * @param cursor         : 이전 페이지 마지막 요소
 * @param size           : 사이즈
 * @param sortField      : 정렬 필드 (ipAddress, at) (Default value : at)
 * @param sortDirection  : 정렬 방향 (asc, desc) (Default value : desc)
 */
public record ChangeLogRequest
        (
                String employeeNumber,
                ChangeType type,
                String memo,
                String ipAddress,

                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                OffsetDateTime atFrom,

                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                OffsetDateTime atTo,

                Long idAfter,
                String cursor,
                Integer size,
                String sortField,
                String sortDirection
        )
{
    public ChangeLogRequest {
        size = (size == null || size <= 0) ? 10 : size;
        sortField = (sortField == null || sortField.isEmpty()) ? "at" : sortField;
        sortDirection = (sortDirection == null || sortDirection.isEmpty()) ? "desc" : sortDirection;

        if (atFrom != null) {
            atFrom = atFrom.atZoneSameInstant(KOREA_ZONE)
                    .withHour(0).withMinute(0).withSecond(0).withNano(0)
                    .toOffsetDateTime();
        }

        if (atTo != null) {
            atTo = atTo.atZoneSameInstant(KOREA_ZONE)
                    .withHour(23).withMinute(59).withSecond(59).withNano(999999999)
                    .toOffsetDateTime();
        }
    }
}