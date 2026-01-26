package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

/**
 * 직원 관리 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface EmployeeService

{

  /**
   * 신규 직원을 시스템에 등록하고 프로필 이미지를 처리합니다.
   *
   * @param request        등록할 직원의 상세 정보 (JSON 데이터)
   * @param profileImage   직원의 프로필 이미지 파일 (MultipartFile)
   * @param servletRequest 클라이언트의 IP 주소 추출을 위한 객체
   * @return 등록이 완료된 직원의 DTO 정보
   */
  EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest);

  /**
   * 기존 직원의 정보를 수정합니다.
   * 규칙: 사원 번호를 제외한 속성 수정 가능, 이메일 중복 체크, 퇴사는 상태 변경으로 처리.
   *
   * @param id             수정할 직원의 고유 ID
   * @param request        수정할 데이터 정보
   * @param profileImage   새로 업로드할 프로필 이미지 (선택 사항)
   * @return 수정된 직원의 DTO 정보
   */
  @Transactional
  EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request, MultipartFile profileImage);

  /**
   * 고유 식별자(ID)를 통해 특정 직원을 삭제합니다.
   * 규칙: 삭제 시 연관된 프로필 이미지 파일도 함께 제거되어야 합니다.
   *
   * @param id 삭제할 직원의 고유 ID
   */
  void deleteEmployee(Long id);

  /**
   * 고유 식별자(ID)를 통해 특정 직원의 상세 정보를 조회합니다.
   *
   * @param id 조회할 직원의 고유 ID
   * @return 조회된 직원의 DTO 정보
   */
  EmployeeDto getEmployeeById(Long id);

  /**
   * 명세서 규격에 따라 다양한 필터 조건과 커서 기반 페이징을 사용하여 직원 목록을 조회합니다.
   *
   * @param nameOrEmail    이름 또는 이메일 검색어 (부분 일치)
   * @param employeeNumber 사원 번호 검색어 (부분 일치)
   * @param departmentName 부서명 검색어 (부분 일치)
   * @param position       직함 검색어 (부분 일치)
   * @param hireDateFrom   조회 시작 입사일
   * @param hireDateTo     조회 종료 입사일
   * @param status         재직 상태 (ACTIVATE, RESIGNED 등)
   * @param idAfter        이전 페이지의 마지막 데이터 ID (커서)
   * @param cursor         암호화된 커서 정보 (필요 시 사용)
   * @param size           조회할 페이지 크기
   * @param sortField      정렬 기준 필드 (name, hireDate 등)
   * @param sortDirection  정렬 방향 (asc, desc)
   * @return 명세서 규격에 맞춘 결과 데이터 및 페이징 정보 (Map)
   */
  Map<String, Object> getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      LocalDate hireDateFrom, LocalDate hireDateTo, EmployeeStatus status,
      Long idAfter, String cursor, int size, String sortField, String sortDirection);

}