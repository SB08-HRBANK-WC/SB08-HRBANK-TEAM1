package com.wc.hr_bank.dto.response.changelog;

import com.wc.hr_bank.entity.ChangeType;

/**
 * 수정 이력 건수 다건 조회 DTO
 * @param id              : 수정 이력 ID 
 * @param type            : 이력 유형 (CREATED, UPDATED, DELETED)
 * @param employeeNumber  : 대상 직원 사번
 * @param memo            : 메모 내용
 * @param ipAddress       : IP 주소
 * @param at              : 수정 이력 생성일
 */
public record ChangeLogDto
(
  Long id,
  ChangeType type,
  String employeeNumber,
  String memo,
  String ipAddress,
  String at
)
{}
