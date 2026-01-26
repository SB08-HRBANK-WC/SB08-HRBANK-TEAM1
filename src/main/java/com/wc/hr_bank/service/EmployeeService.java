package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 직원 관리 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface EmployeeService

{

  /**
   * 신규 직원을 시스템에 등록하고 프로필 이미지를 처리합니다.
   */
  EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest);

  /**
   * 기존 직원의 정보를 수정합니다.
   * 규칙: 사원 번호를 제외한 속성 수정 가능, 이메일 중복 체크, 퇴사는 상태 변경으로 처리.
   */
  EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile profileImage);

  /**
   * 고유 식별자(ID)를 통해 특정 직원을 삭제합니다.
   * 규칙: 삭제 시 연관된 프로필 이미지 파일도 함께 제거되어야 합니다.
   */
  void deleteEmployee(Long id);

  /**
   * 고유 식별자(ID)를 통해 특정 직원의 상세 정보를 조회합니다.
   */
  EmployeeDto getEmployeeById(Long id);

  CursorPageResponseEmployeeDto getEmployees(EmployeeListRequest request);


}