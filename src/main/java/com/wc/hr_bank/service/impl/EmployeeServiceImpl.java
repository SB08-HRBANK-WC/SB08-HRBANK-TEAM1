package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDistDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.Department;
import com.wc.hr_bank.entity.Employee;
import com.wc.hr_bank.entity.EmployeeStatus;
import com.wc.hr_bank.mapper.EmployeeMapper;
import com.wc.hr_bank.repository.DepartmentRepository;
import com.wc.hr_bank.repository.EmployeeRepository;
import com.wc.hr_bank.service.ChangeLogService;
import com.wc.hr_bank.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService

{
  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;
  private final StringHttpMessageConverter stringHttpMessageConverter;
  private final ChangeLogService changeLogService;

  @Override
  @Transactional
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest)

  {
    if (employeeRepository.existsByEmail(request.email()))
    {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }

    Department department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));

    Employee employee = Employee.builder()
        .name(request.name())
        .email(request.email())
        .employeeNumber("EMP-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase())
        .position(request.position())
        .hireDate(request.hireDate() != null ? request.hireDate() : LocalDate.now())
        .status(EmployeeStatus.ACTIVE)
        .department(department)
        .build();
    Employee savedEmployee = employeeRepository.save(employee);
    changeLogService.recordRegistration(savedEmployee, getSafeIp(servletRequest), request.memo());

    return employeeMapper.toDto(savedEmployee);
  }

  public String getSafeIp(HttpServletRequest request) {

    String[] headers = {"X-Forwarded-For", "X-Real-IP"};

    for (String header : headers) {
      String ip = request.getHeader(header);
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
        return ip.contains(",") ? ip.split(",")[0].trim() : ip;
      }
    }

    return request.getRemoteAddr();
  }

  @Override
  @Transactional
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile profileImage,
      HttpServletRequest servletRequest)

  {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. ID: " + id));

    //  보정 1: 수정 전 데이터를 별도 객체(Snapshot)로 복사
    // recordModification에서 변경 전/후를 비교하려면 원본 데이터가 필요합니다
    Employee oldEmployeeSnapshot = Employee.builder()
        .name(employee.getName())
        .email(employee.getEmail())
        .position(employee.getPosition())
        .status(employee.getStatus())
        .department(employee.getDepartment())
        .hireDate(employee.getHireDate())
        .profileImage(employee.getProfileImage())
        .build();

    if (request.email() != null && !request.email().equals(employee.getEmail()))
    {
      if (employeeRepository.existsByEmail(request.email()))
      {
        throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.email());
      }
    }

    Department department = employee.getDepartment();
    if (request.departmentId() != null)
    {
      department = departmentRepository.findById(request.departmentId())
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));
    }

    Employee updatedEmployee = employee.updateEmployee(
        request.name() != null ? request.name() : employee.getName(),
        request.email() != null ? request.email() : employee.getEmail(),
        request.position() != null ? request.position() : employee.getPosition(),
        request.status() != null ? request.status() : employee.getStatus(),
        department,
        employee.getProfileImage(),
        request.hireDate() != null ? request.hireDate() : employee.getHireDate()
    );

    //  보정 2: 스냅샷과 현재 엔티티를 비교하여 기록
    changeLogService.recordModification(oldEmployeeSnapshot,
        employee, getSafeIp(servletRequest), request.memo());

    return employeeMapper.toDto(employee);
  }

  @Override
  @Transactional
  public void deleteEmployee(Long id, HttpServletRequest servletRequest)

  {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));

    changeLogService.recordRemoval(employee, getSafeIp(servletRequest), "직원 삭제");

    employeeRepository.delete(employee);

  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseEmployeeDto getEmployees(EmployeeListRequest request) {
    long totalElements = employeeRepository.count();
    Pageable pageable = PageRequest.of(0, request.size() + 1);

    String sortField = request.sortField();
    boolean isAsc = "asc".equalsIgnoreCase(request.sortDirection());

    // 1. 정렬 필드 및 방향에 따른 분기 처리
    List<Employee> employees = switch (sortField) {
      case "name" -> isAsc
          ? employeeRepository.findByNameWithPagingASC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), request.cursor(), request.idAfter(), pageable)
          : employeeRepository.findByNameWithPagingDESC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), request.cursor(), request.idAfter(), pageable);

      case "hireDate" -> {
        LocalDate cursorDate = (request.cursor() != null && !request.cursor().isBlank()) ? LocalDate.parse(request.cursor()) : null;
        yield isAsc
            ? employeeRepository.findByHireDateWithPagingASC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), cursorDate, request.idAfter(), pageable)
            : employeeRepository.findByHireDateWithPagingDESC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), cursorDate, request.idAfter(), pageable);
      }

      case "employeeNumber" -> isAsc
          ? employeeRepository.findByEmployeeNumberWithPagingASC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), request.cursor(), request.idAfter(), pageable)
          : employeeRepository.findByEmployeeNumberWithPagingDESC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), request.cursor(), request.idAfter(), pageable);

      default -> isAsc
          ? employeeRepository.findByNameWithPagingASC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), request.cursor(), request.idAfter(), pageable)
          : employeeRepository.findByNameWithPagingDESC(request.nameOrEmail(), request.employeeNumber(), request.departmentName(), request.position(), request.status(), request.hireDateFrom(), request.hireDateTo(), request.cursor(), request.idAfter(), pageable);
    };

    // 2. 결과 가공 및 hasNext 판단
    boolean hasNext = employees.size() > request.size();
    List<Employee> resultContent = hasNext ? employees.subList(0, request.size()) : employees;

    List<EmployeeDto> dtoList = resultContent.stream()
        .map(employeeMapper::toDto)
        .toList();

    // 3. buildResponse 호출 (정렬 필드 정보 포함)
    return buildResponse(dtoList, sortField, request.size(), totalElements, hasNext);
  }

  /**
   * 정렬 필드에 따라 nextCursor를 동적으로 생성하도록 보강
   */
  private CursorPageResponseEmployeeDto buildResponse(
      List<EmployeeDto> content, String sortField, int size, long totalElements, boolean hasNext)

  {
    String nextCursor = null;
    Long nextIdAfter = 0L;

    if (!content.isEmpty()) {
      EmployeeDto lastItem = content.get(content.size() - 1);
      nextIdAfter = lastItem.getId();

      // 정렬 필드에 따라 커서 값 세팅 (팀원분 ChangeLog 스타일)
      nextCursor = switch (sortField) {
        case "name" -> lastItem.getName();
        case "hireDate" -> lastItem.getHireDate().toString();
        case "employeeNumber" -> lastItem.getEmployeeNumber();
        default -> lastItem.getName();
      };
    }

    return new CursorPageResponseEmployeeDto(
        content, nextCursor, nextIdAfter, size, totalElements, hasNext);
  }
  /**
   *  EmployeeDto 클래스 호환 버전
   */
  private CursorPageResponseEmployeeDto buildResponse(
      List<EmployeeDto> content,
      int size,
      long totalElements,
      boolean hasNext)

  {
    String nextCursor = null;
    Long nextIdAfter = 0L;

    if (!content.isEmpty()) {
      // 마지막 아이템에서 ID 추출 (클래스 형식이므로 getId() 사용)
      EmployeeDto lastItem = content.get(content.size() - 1);
      nextIdAfter = lastItem.getId();

      // Swagger 명세서 규격에 따른 Base64 인코딩 처리
      String cursorJson = "{\"id\":" + nextIdAfter + "}";
      nextCursor = java.util.Base64.getEncoder().encodeToString(cursorJson.getBytes());
    }

    return new CursorPageResponseEmployeeDto(
        content,
        nextCursor,
        nextIdAfter,
        size,
        totalElements,
        hasNext
    );
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDto getEmployeeById(Long id)

  {
    return employeeRepository.findById(id).map(employeeMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));
  }

  @Override
  @Transactional(readOnly = true)
  public List<EmployeeDistDto> getEmployeesDist(String groupBy, EmployeeStatus status) {
    List<Object[]> dist = employeeRepository.getEmployeesDist(groupBy, status);
    List<EmployeeDistDto> newDist = new ArrayList<>();

    for (Object[] d : dist) {
      String str = (String) d[0];
      Long lon = ((Number) d[1]).longValue();
      Double dbl = ((Number) d[2]).doubleValue();

      newDist.add(employeeMapper.toEmployeeDistDto(str, lon, dbl));
    }

    return newDist;
  }
}