package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.EmployeeApi;
import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import com.wc.hr_bank.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi

{

  private final EmployeeService employeeService;

  @Override
  @PostMapping
  public ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("request") EmployeeCreateRequest request,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
      HttpServletRequest servletRequest)

  {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(employeeService.createEmployee(request, profileImage, servletRequest));
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @RequestPart("request") EmployeeUpdateRequest request,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage)

  {
    return ResponseEntity.ok(employeeService.updateEmployee(id, request, profileImage));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id)

  {
    employeeService.deleteEmployee(id);
    // 명세서 규격: 삭제 성공 시 204 반환
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Map<String, Object>> getEmployees(String nameOrEmail, String employeeNumber,
      String departmentName, String position, LocalDate hireDateFrom, LocalDate hireDateTo,
      EmployeeStatus status, Long idAfter, int size, String sortField, String sortDirection) {
    return null;
  }

  @Override
  public ResponseEntity<Map<String, Object>> getEmployees(String nameOrEmail, String employeeNumber,
      String departmentName, String position, LocalDate hireDateFrom, LocalDate hireDateTo,
      EmployeeStatus status, Long idAfter, String cursor, int size, String sortField,
      String sortDirection) {
    return null;
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id)

  {
    return ResponseEntity.ok(employeeService.getEmployeeById(id));
  }

  /**
   * 피드백 반영: 개별 파라미터 대신 EmployeeListRequest(Record)를 사용하여 코드를 단순화하고 서비스 계층으로 전달합니다.
   */
  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(EmployeeListRequest request)

  {
    return ResponseEntity.ok(employeeService.getEmployees(request));
  }

}