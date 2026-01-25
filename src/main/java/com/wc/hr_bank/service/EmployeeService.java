package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService
{

  /**
   * 신규 직원을 등록합니다.
   * @param request 직원 정보 (JSON)
   * @param profileImage 프로필 이미지 파일 (Multipart)
   * @param servletRequest IP 추출을 위한 서블릿 요청 객체
   */
  EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, HttpServletRequest servletRequest);

  /**
   * ID로 직원의 상세 정보를 조회합니다.
   * @param id 직원 고유 식별자
   */
  EmployeeDto getEmployeeById(Long id);

  /**
   * 명세서 규격에 따라 필터링된 직원 목록을 커서 기반(No-Offset) 방식으로 조회합니다.
   * @param nameOrEmail 직원 이름 또는 이메일 (부분 일치)
   * @param employeeNumber 사원 번호 (부분 일치)
   * @param departmentName 부서 이름 (부분 일치)
   * @param position 직함 (부분 일치)
   * @param hireDateFrom 입사일 범위 시작
   * @param hireDateTo 입사일 범위 종료
   * @param status 재직 상태 (완전 일치)
   * @param idAfter 이전 페이지 마지막 요소 ID
   * @param cursor 암호화된 커서 문자열 (다음 페이지 시작점)
   * @param size 페이지 크기
   * @param sortField 정렬 필드 (name, employeeNumber, hireDate)
   * @param sortDirection 정렬 방향 (asc, desc)
   * @return 명세서 규격(content, nextIdAfter, hasNext 등)이 포함된 결과 맵
   */
  Map<String, Object> getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      LocalDate hireDateFrom, LocalDate hireDateTo, EmployeeStatus status,
      Long idAfter, String cursor, int size, String sortField, String sortDirection);
}