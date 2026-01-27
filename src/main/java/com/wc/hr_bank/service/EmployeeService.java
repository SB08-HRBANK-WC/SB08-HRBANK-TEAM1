package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDistDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeTrendDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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
  EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest);

  /**
   * 고유 식별자(ID)를 통해 특정 직원을 삭제합니다.
   * 규칙: 삭제 시 연관된 프로필 이미지 파일도 함께 제거되어야 합니다.
   */
  void deleteEmployee(Long id, HttpServletRequest servletRequest);

  /**
   * 고유 식별자(ID)를 통해 특정 직원의 상세 정보를 조회합니다.
   */
  EmployeeDto getEmployeeById(Long id);

  CursorPageResponseEmployeeDto getEmployees(EmployeeListRequest request);

  /**
   * 직원의 부서별 또는 직무별 인원 분포를 집계합니다.
   *
   * @param groupBy 집계 기준 (기준: 'department' 또는 'jobTitle') - 기본값: department
   * @param status  조회할 직원의 상태 (예: 재직, 퇴사 등) - 기본값: 재직
   * @return 기준별 인원수와 그룹명을 담은 통계 DTO 리스트
   */
  List<EmployeeDistDto> getEmployeesDist(String groupBy, EmployeeStatus status);

  /**
   * 직원의 상태, 입사 시작일, 입사 종료일 기준으로 총합 인원수를 구합니다.
   *
   * @param status
   * @param fromDate
   * @param toDate
   * @return
   */
  Long countByPeriod(EmployeeStatus status, LocalDate fromDate, LocalDate toDate);

  /**
   * 지정된 기간 및 시간 단위로 그룹화된 직원 수 추이를 조회
   *
   * @param fromDate
   * @param toDate
   * @param unit
   * @return
   */
  List<EmployeeTrendDto> getEmployeeTrend(LocalDate fromDate, LocalDate toDate, String unit);
}