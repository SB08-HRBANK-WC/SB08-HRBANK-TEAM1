package com.wc.hr_bank.service.impl;


import com.wc.hr_bank.dto.request.changelog.EmployeeLogRequest;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDto;
import com.wc.hr_bank.dto.response.changelog.CursorPageResponseChangeLogDto;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService
{
  private final ChangeLogRepository changeLogRepository;
  private final ChangeLogMapper changeLogMapper;

  /**
   * 직원 정보 수정 이력 목록 조회 (커서 페이지네이션)
   * @param request
   * @return
   */
  @Override
  public CursorPageResponseChangeLogDto getChangeLogs(EmployeeLogRequest request) {
    Pageable pageable = PageRequest.of(0, request.size());

    // 정렬 필드 - ipAddress, at
    String sortField = request.sortField();

    // 정렬 방향 - asc, desc
    String sortDirection = request.sortDirection();

    String nextCursor = null;
    Long nextIdAfter = 0L;
    List<ChangeLog> logs = List.of();

    String employeeNumber = request.employeeNumber();
    ChangeType type = request.type();
    String memo = request.memo();
    String ipAddress = request.ipAddress();

    Instant atFrom = convertToInstant(request.atFrom());
    Instant atTo = convertToInstant(request.atTo());

    Long idAfter = request.idAfter();
    String cursor = request.cursor();

    logs = switch (sortField) {
      case "at" -> {
        Instant cursorAt = (cursor != null)
            ? convertToInstant(LocalDateTime.parse(request.cursor()))
            : null;

        yield "desc".equals(sortDirection)
            ? changeLogRepository.findByAtDesc(employeeNumber, type, memo, ipAddress, atFrom, atTo, cursorAt, idAfter, pageable)
            : changeLogRepository.findByAtAsc(employeeNumber, type, memo, ipAddress, atFrom, atTo, cursorAt, idAfter, pageable);
      }

      case "ipAddress" -> {
        yield "desc".equals(sortDirection)
            ? changeLogRepository.findByIpDesc(employeeNumber, type, memo, ipAddress, atFrom, atTo, cursor, idAfter, pageable)
            : changeLogRepository.findByIpAsc(employeeNumber, type, memo, ipAddress, atFrom, atTo, cursor, idAfter, pageable);
      }
      default -> List.of();
    };


    List<ChangeLogDto> content = logs.stream()
        .map(changeLogMapper::toLogDto)
        .toList();

    boolean hasNext = content.size() >= request.size();

    if (!content.isEmpty()) {
      ChangeLogDto lastItem = content.get(content.size() - 1);
      nextCursor = (sortField.equals("ipAddress")) ? lastItem.ipAddress() : lastItem.at();
      nextIdAfter = lastItem.id();
    }

    Long totalElements = changeLogRepository.countByFilters(employeeNumber, type, memo, ipAddress, atFrom, atTo);

    return changeLogMapper.toCursorPageResponse(
        content,
        nextCursor,
        nextIdAfter,
        request.size(),
        totalElements,
        hasNext
    );
  }

  /**
   * DateTime -> Instant 변환
   * @param datetime
   * @return
   */
  private Instant convertToInstant(LocalDateTime datetime) {
    Instant instant = datetime.atZone(ZoneId.systemDefault()).toInstant();
    return instant;
  }

  /**
   * 특정 이력 상세 정보 조회
   * @param id  이력 ID
   * @return    상세 조회 DTO
   */
  @Override
  public ChangeLogDetailDto getChangeLogDetail(Long id) {
    return changeLogRepository.findWithDiffsById(id)
        .map(changeLogMapper::toDto)
        .orElseThrow(() -> new EntityNotFoundException("해당 이력을 찾을 수 없습니다."));
  }

  /**
   * 기간 별 수정 이력 개수 조회
   * @param from
   * @param to
   * @return
   */
  @Override
  public Long countByPeriod(LocalDateTime from, LocalDateTime to) {
    // LocalDateTime -> Instant
    Instant fromInstant = from.atZone(ZoneId.of("Asia/Seoul")).toInstant();
    Instant toInstant = to.atZone(ZoneId.of("Asia/Seoul")).toInstant();

    return changeLogRepository.countLogsByPeriod(fromInstant, toInstant);
  }

  /**
   * 직원 생성 (수정 이력)
   * @param e
   * @param ip
   * @param memo
   */
  @Override
  @Transactional
  public void recordRegistration(Employee e, String ip, String memo) {
    processRecording(null, e, ip, memo, ChangeType.CREATED);
  }

  /**
   * 직원 수정 (수정 이력)
   * @param oldEmp
   * @param newEmp
   * @param ip
   * @param memo
   */
  @Override
  @Transactional
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
  @Transactional
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
  private <Employee, R> String transform(Employee target, Function<Employee, R> mapper) {
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
