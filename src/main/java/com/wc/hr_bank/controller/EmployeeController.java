package com.wc.hr_bank.controller;

import com.wc.hr_bank.api.EmployeeApi;
import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import com.wc.hr_bank.service.EmployeeService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController implements EmployeeApi
{

  private final EmployeeService employeeService;

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("employee") EmployeeCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage,
      HttpServletRequest servletRequest
  )
  {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(employeeService.createEmployee(request, profileImage, servletRequest));
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id)
  {
    return ResponseEntity.ok(employeeService.getEmployeeById(id));
  }

  @Override
  public ResponseEntity<Map<String, Object>> getEmployees(String nameOrEmail, String employeeNumber,
      String departmentName, String position, LocalDate hireDateFrom, LocalDate hireDateTo,
      EmployeeStatus status, Long idAfter, int size, String sortField, String sortDirection) {
    return null;
  }

  @Override
  @GetMapping
  public ResponseEntity<Map<String, Object>> getEmployees(
      @RequestParam(required = false) String nameOrEmail,
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String departmentName,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateTo,
      @RequestParam(required = false) EmployeeStatus status,
      @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)")
      @RequestParam(required = false) String cursor,

      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "hireDate") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection
  )
  {
    // 서비스 호출 시 cursor 파라미터도 함께 전달합니다.
    Map<String, Object> result = employeeService.getEmployees(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status, idAfter, cursor, size, sortField, sortDirection
    );
    return ResponseEntity.ok(result);
  }
}