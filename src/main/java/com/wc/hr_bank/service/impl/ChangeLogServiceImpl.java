package com.wc.hr_bank.service.impl;


import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.entity.ChangeLog;
import com.wc.hr_bank.entity.ChangeType;
import com.wc.hr_bank.entity.Department;
import com.wc.hr_bank.entity.Employee;
import com.wc.hr_bank.entity.LogPropertyType;
import com.wc.hr_bank.entity.base.BaseEntity;
import com.wc.hr_bank.mapper.ChangeLogMapper;
import com.wc.hr_bank.repository.ChangeLogRepository;
import com.wc.hr_bank.service.ChangeLogService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;
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
   * @param e
   * @param ip
   * @param memo
   */
  @Override
  public void recordRegistration(Employee e, String ip, String memo) {
    processRecording(null, e, ip, memo, ChangeType.UPDATED);
  }

  /**
   * 직원 수정 (수정 이력)
   * @param oldEmp
   * @param newEmp
   * @param ip
   * @param memo
   */
  @Override
  public void recordModification(Employee oldEmp, Employee newEmp, String ip, String memo) {
    processRecording(oldEmp, newEmp, ip, memo, ChangeType.UPDATED);
  }

  /**
   * 직원 삭제 (수정 이력)
   * @param e
   * @param ip
   * @param memo
   */
  @Override
  public void recordRemoval(Employee e, String ip, String memo) {
    processRecording(e, null, ip, memo, ChangeType.DELETED);
  }

  /**
   * 전체적인 세팅 및 저장
   * @param oldE
   * @param newE
   * @param ip
   * @param memo
   * @param type
   */
  private void processRecording(Employee oldE, Employee newE, String ip, String memo, ChangeType type) {
    Employee target = (type == ChangeType.DELETED) ? oldE : newE;

    ChangeLog changeLog = createBaseLog(target, ip, memo, type);

    saveLog(changeLog, oldE, newE, type);

    changeLogRepository.save(changeLog);
  }

  /**
   * 필드 별 비교 및 추가
   * @param log
   * @param oldE
   * @param newE
   * @param type
   */
  private void saveLog(ChangeLog log, Employee oldE, Employee newE, ChangeType type) {
    compareAndRecord(log, LogPropertyType.EMPLOYEE_NAME, oldE, newE, Employee::getName, type);

    compareAndRecord(log, LogPropertyType.EMAIL, oldE, newE, Employee::getEmail, type);

    compareAndRecord(log, LogPropertyType.JOB_TITLE, oldE, newE, Employee::getJobTitle, type);

    compareAndRecord(log, LogPropertyType.JOINED_AT, oldE, newE, Employee::getJoinedAt, type);

    compareAndRecord(log, LogPropertyType.STATUS, oldE, newE, Employee::getStatus, type);

    compareAndRecord(log, LogPropertyType.DEPARTMENT, oldE, newE,
        emp -> Optional.ofNullable(
            emp.getDepartment())
            .map(Department::getName)
            .orElse(null), type);

    compareAndRecord(log, LogPropertyType.PROFILE_IMAGE, oldE, newE,
        emp -> Optional.ofNullable(
            emp.getProfileImage())
            .map(BaseEntity::getId)
            .orElse(null), type);
  }

  /**
   * 엔티티의 특정 속성 변화를 감지하고 변경 이력을 기록
   * @param changeLog
   * @param property
   * @param oldEmp
   * @param newEmp
   * @param extractor
   * @param type
   */
  private void compareAndRecord(
      ChangeLog changeLog,
      LogPropertyType property,
      Employee oldEmp,
      Employee newEmp,
      Function<Employee, Object> extractor,
      ChangeType type
  ) {

    String beforeValue = transform(oldEmp, extractor);
    String afterValue = transform(newEmp, extractor);

    if (beforeValue == null && afterValue == null) return;

    boolean isDifferent = !Objects.equals(beforeValue, afterValue);

    if (type != ChangeType.UPDATED || isDifferent) {
      changeLog.addDiff(property, beforeValue, afterValue);
    }
  }

  /**
   * 함수형 헬퍼 메서드 -> 값 필터링
   * @param target
   * @param mapper
   * @return
   * @param <T>
   * @param <R>
   */
  private <T, R> String transform(T target, Function<T, R> mapper) {
    return Optional.ofNullable(target)
        .map(mapper)
        .map(String::valueOf)
        .orElse(null);
  }

  /**
   * 기본 ChangeLog 세팅
   * @param e
   * @param ip
   * @param memo
   * @param changeType
   * @return
   */
  private ChangeLog createBaseLog(Employee e, String ip, String memo, ChangeType changeType) {
    return ChangeLog.create(e.getId(),
        e.getName(),
        e.getEmployeeNumber(),
        memo,
        changeType,
        ip);
  }
}
