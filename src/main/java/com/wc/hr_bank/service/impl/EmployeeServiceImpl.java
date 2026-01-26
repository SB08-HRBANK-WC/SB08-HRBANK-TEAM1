package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.Department;
import com.wc.hr_bank.entity.Employee;
import com.wc.hr_bank.entity.EmployeeStatus;
import com.wc.hr_bank.mapper.EmployeeMapper;
import com.wc.hr_bank.repository.DepartmentRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService

{

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;

  @Override
  @Transactional
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest)

  {
    if (employeeRepository.existsByEmail(request.getEmail()))

    {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.getEmail());
    }

    Department department = departmentRepository.findById(request.getDepartmentId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다. ID: " + request.getDepartmentId()));

    String generatedEmployeeNumber = "EMP-" + LocalDate.now().getYear() + "-" +
        UUID.randomUUID().toString().substring(0, 5).toUpperCase();

    Employee employee = Employee.builder()
        .name(request.getName())
        .email(request.getEmail())
        .employeeNumber(generatedEmployeeNumber)
        .position(request.getPosition())
        .hireDate(request.getHireDate() != null ? request.getHireDate() : LocalDate.now())
        .status(EmployeeStatus.ACTIVATE)
        .department(department)
        .build();

    Employee savedEmployee = employeeRepository.save(employee);
    log.info("신규 직원 등록 완료: {}, 사번: {}", savedEmployee.getName(), savedEmployee.getEmployeeNumber());

    return employeeMapper.toDto(savedEmployee);
  }

  @Transactional
  @Override
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile profileImage)

  {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. ID: " + id));

    // 이메일 중복 체크 (본인 제외)
    if (request.getEmail() != null && !request.getEmail().equals(employee.getEmail()))

    {
      if (employeeRepository.existsByEmail(request.getEmail()))
      {
        throw new IllegalArgumentException("이미 사용 중인 이메일입니다: " + request.getEmail());
      }
    }

    Department department = employee.getDepartment();
    if (request.getDepartmentId() != null)
    {
      department = departmentRepository.findById(request.getDepartmentId())
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 부서입니다."));
    }

    // 엔티티 업데이트 (사번 제외)
    employee.updateEmployee(
        request.getName() != null ? request.getName() : employee.getName(),
        request.getEmail() != null ? request.getEmail() : employee.getEmail(),
        request.getPosition() != null ? request.getPosition() : employee.getPosition(),
        request.getStatus() != null ? request.getStatus() : employee.getStatus(),
        department,
        employee.getProfileImage()
    );

    log.info("직원 정보 수정 완료: ID {}", id);
    return employeeMapper.toDto(employee);
  }

  @Override
  @Transactional
  public void deleteEmployee(Long id)

  {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. ID: " + id));

    // 요구사항: 삭제 시 프로필 이미지도 함께 삭제
    if (employee.getProfileImage() != null)

    {
      log.info("직원 삭제와 함께 프로필 이미지(ID: {})를 삭제합니다.", employee.getProfileImage().getId());
      // 실제 파일 및 엔티티 삭제 로직 필요 시 구현
    }

    employeeRepository.delete(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDto getEmployeeById(Long id)

  {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다. ID: " + id));
    return employeeMapper.toDto(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public Map<String, Object> getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      LocalDate hireDateFrom, LocalDate hireDateTo, EmployeeStatus status,
      Long idAfter, String cursor, int size, String sortField, String sortDirection)

  {
    String actualSortField = "hireDate";
    if (sortField != null)

    {
      switch (sortField)

      {
        case "joinedAt":
        case "hireDate": actualSortField = "hireDate"; break;
        case "position": actualSortField = "position"; break;
        default: actualSortField = sortField;
      }
    }

    Sort sort = "desc".equalsIgnoreCase(sortDirection)
        ? Sort.by(actualSortField).descending()
        : Sort.by(actualSortField).ascending();

    Pageable pageable = PageRequest.of(0, size, sort);

    List<Employee> employees = employeeRepository.findEmployeesByFilters(
        nameOrEmail, employeeNumber, departmentName, position, status,
        hireDateFrom, hireDateTo, idAfter, pageable
    );

    List<EmployeeDto> content = employees.stream()
        .map(employeeMapper::toDto)
        .collect(Collectors.toList());

    Map<String, Object> response = new HashMap<>();
    response.put("content", content);
    response.put("nextCursor", cursor);
    response.put("nextIdAfter", content.isEmpty() ? null : content.get(content.size() - 1).getId());
    response.put("size", size);
    response.put("hasNext", content.size() >= size);

    return response;
  }

}