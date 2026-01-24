package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.entity.Employee;
import java.time.LocalDateTime;

public interface ChangeLogService
{
  /**
   * 수정 이력 상세 조회
   * @param id
   * @return
   */
  ChangeLogDetailDto getChangeLogDetail(Long id);

  /**
   * 기간 별 수정 이력 개수 조회
   * @param from
   * @param to
   * @return
   */
  Long countByPeriod(LocalDateTime from, LocalDateTime to);

  /**
   * 직원 생성 (수정 이력 추가)
   * @param employee
   * @param ip
   * @param memo
   */
  void recordRegistration(Employee employee, String ip, String memo);

  /**
   * 직원 수정 (수정 이력 추가)
   * @param oldEmployee
   * @param newEmployee
   * @param ip
   * @param memo
   */
  void recordModification(Employee oldEmployee, Employee newEmployee, String ip, String memo);

  /**
   * 직원 삭제 (수정 이력 추가)
   * @param employee
   * @param ip
   * @param memo
   */
  void recordRemoval(Employee employee, String ip, String memo);
}
