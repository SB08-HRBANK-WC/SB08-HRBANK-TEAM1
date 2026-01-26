package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.EmployeeApi;
import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import com.wc.hr_bank.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id)

  {
    return ResponseEntity.ok(employeeService.getEmployeeById(id));
  }

  @Override
  @GetMapping
  public ResponseEntity<Map<String, Object>> getEmployees(
      @RequestParam(required = false) String nameOrEmail,
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String departmentName,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) LocalDate hireDateFrom,
      @RequestParam(required = false) LocalDate hireDateTo,
      @RequestParam(required = false) EmployeeStatus status,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "hireDate") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection)

  {
    return ResponseEntity.ok(employeeService.getEmployees(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status, idAfter, cursor, size, sortField, sortDirection));
  }

}