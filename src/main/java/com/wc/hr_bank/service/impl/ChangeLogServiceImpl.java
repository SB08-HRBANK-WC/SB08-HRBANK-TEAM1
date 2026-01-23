package com.wc.hr_bank.service.impl;


import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.entity.ChangeLog;
import com.wc.hr_bank.entity.ChangeType;
import com.wc.hr_bank.entity.Employee;
import com.wc.hr_bank.entity.LogPropertyType;
import com.wc.hr_bank.mapper.ChangeLogMapper;
import com.wc.hr_bank.repository.ChangeLogRepository;
import com.wc.hr_bank.service.ChangeLogService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService
{
  private final ChangeLogRepository changeLogRepository;
  private final ChangeLogMapper changeLogMapper;

  /**
   * 특정 이력 상세 정보 조회
   *
   * @param id  이력 ID
   * @return    상세 조회 DTO
   */
  @Override
  @Transactional(readOnly = true)
  public ChangeLogDetailDto getChangeLogDetail(Long id) {
    return changeLogRepository.findWithDiffsById(id)
        .map(changeLogMapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("해당 이력을 찾을 수 없습니다."));
  }

  /**
   * 직원 생성 (수정 이력)
   * @param employee
   * @param ip
   * @param memo
   */
  @Override
  public void recordRegistration(Employee employee, String ip, String memo) {
  }

  @Override
  public void recordModification(Employee oldEmp, Employee newEmp, String ip, String memo) {
  }

  @Override
  public void recordRemoval(Employee employee, String ip, String memo) {
  }
}
