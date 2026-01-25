package com.wc.hr_bank.api;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Map;

@Tag(name = "Employee", description = "인사 관리 시스템의 직원 정보 등록 및 조회를 담당하는 API입니다.")
public interface EmployeeApi
{

  @Operation(summary = "신규 직원 등록", description = "직원의 인적 사항과 프로필 이미지를 등록합니다. 이메일은 중복될 수 없으며 사번은 자동 생성됩니다.")
  ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(description = "등록할 직원의 상세 정보 (JSON)") EmployeeCreateRequest request,
      @Parameter(description = "직원의 프로필 이미지 파일 (Multipart)") MultipartFile profileImage,
      HttpServletRequest servletRequest
  );

  @Operation(summary = "직원 상세 정보 조회", description = "직원의 고유 ID를 사용하여 해당 직원의 모든 상세 정보를 조회합니다.")
  ResponseEntity<EmployeeDto> getEmployeeById(
      @Parameter(description = "조회할 직원의 식별자 (ID)") Long id
  );

  ResponseEntity<Map<String, Object>> getEmployees(String nameOrEmail, String employeeNumber,
      String departmentName, String position, LocalDate hireDateFrom, LocalDate hireDateTo,
      EmployeeStatus status, Long idAfter, int size, String sortField, String sortDirection);

  @Operation(summary = "직원 목록 검색 및 페이징", description = "다양한 필터 조건을 조합하여 직원 목록을 조회합니다. No-Offset 방식을 사용하여 대용량 데이터에서도 성능이 최적화되어 있습니다.")
  ResponseEntity<Map<String, Object>> getEmployees(
      @Parameter(description = "이름 또는 이메일 (부분 일치)") String nameOrEmail,
      @Parameter(description = "사원 번호 (부분 일치)") String employeeNumber,
      @Parameter(description = "부서명 (부분 일치)") String departmentName,
      @Parameter(description = "직함/직책 (부분 일치)") String position,
      @Parameter(description = "입사일 검색 시작일 (yyyy-MM-dd)") LocalDate hireDateFrom,
      @Parameter(description = "입사일 검색 종료일 (yyyy-MM-dd)") LocalDate hireDateTo,
      @Parameter(description = "재직 상태 (ACTIVE, ON_LEAVE, RESIGNED)") EmployeeStatus status,
      @Parameter(description = "이전 페이지의 마지막 데이터 ID ") Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") String cursor,
      @Parameter(description = "한 페이지에 표시할 데이터 개수") int size,
      @Parameter(description = "정렬 기준 필드 (name, employeeNumber, hireDate)") String sortField,
      @Parameter(description = "정렬 방향 (asc, desc)") String sortDirection
  );
}