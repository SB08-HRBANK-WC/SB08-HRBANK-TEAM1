package com.wc.hr_bank.dto.response.changelog;

import com.wc.hr_bank.entity.ChangeType;
import java.util.List;
/**
 *  직원 정보 수정 이력 상세 (상세 조회용)
 * @param id              : 수정 이력 ID
 * @param type            : 유형 (직원 추가, 정보 수정, 직원 삭제)
 * @param employeeNumber  : 직원 사번
 * @param memo            : 내용(비고)
 * @param ipAddress       : IP 주소
 * @param at              : 수정 일시
 * @param employeeName    : 직원 이름
 * @param profileImageId  : 프로필 이미지 ID
 * @param diffs           : 변경 상세 내용
 */
public record ChangeLogDetailDto
(
  Long id,
  ChangeType type,
  String employeeNumber,
  String memo,
  String ipAddress,
  String at,
  String employeeName,
  Long profileImageId,
  List<DiffDto> diffs
)
{
  // 상세 내역 위한 내부 레코드
  public record DiffDto(
      String propertyName,
      String before,
      String after
  ) {}
}