package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.*;
import com.wc.hr_bank.repository.EmployeeRepository;
import com.wc.hr_bank.repository.DepartmentRepository;
import com.wc.hr_bank.repository.ChangeLogRepository;
import com.wc.hr_bank.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService
{

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final ChangeLogRepository changeLogRepository;

  @Override
  @Transactional
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest)
  {
    String ipAddress = getClientIp(servletRequest);

    if (employeeRepository.existsByEmail(request.getEmail()))
    {
      throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
    }

    String employeeNumber = generateEmployeeNumber(request.getHireDate());

    Employee employee = Employee.builder()
        .name(request.getName())
        .email(request.getEmail())
        .jobTitle(request.getPosition())
        .joinedAt(request.getHireDate())
        .status(EmployeeStatus.EMPLOYED)
        .employeeNumber(employeeNumber)
        .build();

    Employee savedEmployee = employeeRepository.save(employee);
    saveRegistrationHistory(savedEmployee, request.getMemo(), ipAddress);

    return convertToDto(savedEmployee);
  }

  @Override
  public EmployeeDto getEmployeeById(Long id)
  {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));
    return convertToDto(employee);
  }

  @Override
  public Map<String, Object> getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      LocalDate hireDateFrom, LocalDate hireDateTo, EmployeeStatus status,
      Long idAfter, String cursor, int size, String sortField, String sortDirection)
  {
    // 1. 커서(cursor) 우선순위 적용: 커서가 있으면 해석하여 idAfter로 사용
    Long effectiveIdAfter = (cursor != null && !cursor.isEmpty()) ? decodeCursor(cursor) : idAfter;

    // 2. 검색 조건 및 정렬 설정
    LocalDate start = (hireDateFrom != null) ? hireDateFrom : LocalDate.of(1900, 1, 1);
    LocalDate end = (hireDateTo != null) ? hireDateTo : LocalDate.of(2100, 12, 31);

    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
    // No-Offset 페이징을 위해 항상 id를 보조 정렬 기준으로 사용
    Pageable pageable = PageRequest.of(0, size + 1, Sort.by(direction, sortField, "id"));

    // 3. 데이터 조회
    List<Employee> employees = employeeRepository.findEmployeesByFilters(
        nameOrEmail, employeeNumber, departmentName, position, status, start, end, effectiveIdAfter, pageable
    );

    // 4. 다음 페이지 정보 계산
    boolean hasNext = employees.size() > size;
    List<Employee> content = hasNext ? employees.subList(0, size) : employees;

    Long lastId = content.isEmpty() ? 0L : content.get(content.size() - 1).getId();
    String nextCursor = hasNext ? encodeCursor(lastId) : "";

    // 5. 명세서 규격 응답 생성
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("content", content.stream().map(this::convertToDto).collect(Collectors.toList()));
    response.put("nextCursor", nextCursor);
    response.put("nextIdAfter", hasNext ? lastId : 0);
    response.put("size", size);
    response.put("totalElements", employeeRepository.count());
    response.put("hasNext", hasNext);

    return response;
  }

  private EmployeeDto convertToDto(Employee employee)
  {
    return EmployeeDto.builder()
        .id(employee.getId())
        .name(employee.getName())
        .email(employee.getEmail())
        .employeeNumber(employee.getEmployeeNumber())
        .departmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null)
        .departmentName(employee.getDepartment() != null ? employee.getDepartment().getName() : "미소속")
        .position(employee.getJobTitle())
        .hireDate(employee.getJoinedAt())
        .status(employee.getStatus() == EmployeeStatus.EMPLOYED ? "재직중" : "기타")
        .profileImageId(employee.getProfileImage() != null ? employee.getProfileImage().getId() : null)
        .build();
  }

  private String encodeCursor(Long id)
  {
    String rawCursor = "{\"id\":" + id + "}";
    return Base64.getEncoder().encodeToString(rawCursor.getBytes());
  }

  private Long decodeCursor(String cursor)
  {
    try
    {
      byte[] decodedBytes = Base64.getDecoder().decode(cursor);
      String decodedString = new String(decodedBytes);
      return Long.parseLong(decodedString.replaceAll("[^0-9]", ""));
    }
    catch (Exception e)
    {
      return null;
    }
  }

  private String generateEmployeeNumber(LocalDate joinedAt)
  {
    int year = joinedAt.getYear();
    long count = employeeRepository.countByJoinedAtBetween(LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31)) + 1;
    return String.format("%d-%03d", year, (int)count);
  }

  private void saveRegistrationHistory(Employee employee, String memo, String ipAddress)
  {
    // TODO: ChangeLogRepository 구현
  }

  private String getClientIp(HttpServletRequest request)
  {
    String ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
    {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}