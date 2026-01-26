package com.wc.hr_bank.dto.request.changelog;

import com.wc.hr_bank.entity.ChangeType;
import java.time.LocalDateTime;
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
      LocalDateTime atFrom,

      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime atTo,

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
  }
}
