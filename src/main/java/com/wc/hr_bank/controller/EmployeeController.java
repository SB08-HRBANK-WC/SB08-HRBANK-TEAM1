package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.EmployeeApi;
import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi

{

  private final EmployeeService employeeService;

  @Override
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("request") EmployeeCreateRequest request,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
      HttpServletRequest servletRequest)

  {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(employeeService.createEmployee(request, profileImage, servletRequest));
  }

  @Override
  @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @RequestPart("request") EmployeeUpdateRequest request,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
      HttpServletRequest servletRequest)

  {
    return ResponseEntity.ok(employeeService.updateEmployee(id, request, profileImage, servletRequest));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, HttpServletRequest servletRequest)

  {
    employeeService.deleteEmployee(id, servletRequest);
    // 명세서 규격: 삭제 성공 시 204 반환
    return ResponseEntity.noContent().build();
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
  public ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(@ModelAttribute EmployeeListRequest request)

  {
    CursorPageResponseEmployeeDto employees = employeeService.getEmployees(request);
    return ResponseEntity.ok(employees);
  }

}