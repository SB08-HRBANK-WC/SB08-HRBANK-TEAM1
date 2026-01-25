package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.Employee;
import com.wc.hr_bank.entity.EmployeeStatus;
import com.wc.hr_bank.mapper.EmployeeMapper;
import com.wc.hr_bank.repository.EmployeeRepository;
import com.wc.hr_bank.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 직원 정보 관리 요구사항을 준수하는 서비스 구현체입니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService

{

  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;

  /**
   * 신규 직원을 등록합니다.
   * 이메일 중복 체크, 사번 자동 생성, 상태 '재직중' 초기화 로직을 수행합니다.
   */
  @Override
  @Transactional
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest)

  {
    // 1. 이메일 중복 체크
    if (employeeRepository.existsByEmail(request.getEmail()))

    {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.getEmail());
    }

    // 2. 사원 번호 자동 생성 (예: EMP-2026-랜덤값)
    String generatedEmployeeNumber = "EMP-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

    // 3. 엔티티 생성 및 기본값 설정 (상태는 자동으로 ACTIVE/재직중 설정)
    // TODO: 승원님이 백업하신 엔티티 생성 로직(빌더 등)을 여기에 구현하세요.

    log.info("신규 직원 등록 완료: {}, 사번: {}", request.getName(), generatedEmployeeNumber);
    return null; // 승원님의 saved 엔티티 변환 결과 반환
  }

  /**
   * ID를 통해 직원의 상세 정보를 조회합니다.
   */
  @Override
  @Transactional(readOnly = true)
  public EmployeeDto getEmployeeById(Long id)

  {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. ID: " + id));

    return employeeMapper.toDto(employee);
  }

  /**
   * 명세서 규격에 따라 필터링된 직원 목록을 커서 기반 방식으로 조회합니다.
   * Repository의 요구 규격(9개 파라미터)에 맞춰 데이터를 전달합니다.
   */
  @Override
  @Transactional(readOnly = true)
  public Map<String, Object> getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      LocalDate hireDateFrom, LocalDate hireDateTo, EmployeeStatus status,
      Long idAfter, String cursor, int size, String sortField, String sortDirection)

  {
    // 1. 정렬 조건 설정
    Sort sort = sortDirection.equalsIgnoreCase("desc")
        ? Sort.by(sortField).descending()
        : Sort.by(sortField).ascending();

    // 2. Pageable 객체 생성 (팀원 Repository 규격 대응)
    Pageable pageable = PageRequest.of(0, size, sort);

    // 3. Repository 호출 (순서: nameOrEmail, employeeNumber, departmentName, position, status, hireDateFrom, hireDateTo, idAfter, pageable)
    List<Employee> employees = employeeRepository.findEmployeesByFilters(
        nameOrEmail,
        employeeNumber,
        departmentName,
        position,
        status,
        hireDateFrom,
        hireDateTo,
        idAfter,
        pageable
    );

    // 4. DTO 변환
    List<EmployeeDto> content = employees.stream()
        .map(employeeMapper::toDto)
        .collect(Collectors.toList());

    // 5. 응답 맵 구성 (명세서 규격 준수)
    Map<String, Object> response = new HashMap<>();
    response.put("content", content);
    response.put("nextCursor", cursor);
    response.put("nextIdAfter", content.isEmpty() ? null : content.get(content.size() - 1).getId());
    response.put("size", size);
    response.put("hasNext", content.size() >= size);

    return response;
  }
}