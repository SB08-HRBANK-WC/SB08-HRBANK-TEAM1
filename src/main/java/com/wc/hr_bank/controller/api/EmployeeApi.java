package com.wc.hr_bank.controller.api;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.Map;

@Tag(name = "직원 관리", description = "직원 관리 API")
public interface EmployeeApi
{

  @Operation(summary = "직원 등록", description = "새로운 직원을 등록합니다.")
  ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(description = "등록할 직원의 상세 정보 (JSON)") EmployeeCreateRequest request,
      @Parameter(description = "직원의 프로필 이미지 파일 (Multipart)") MultipartFile profileImage,
      HttpServletRequest servletRequest
  );
  /**
   * 이미지 가이드 반영: summary(제목)와 description(상세 설명) 추가
   */
  @Operation(summary = "직원 수정", description = "직원 정보를 수정합니다.")
  @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
  ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @RequestPart("employee") EmployeeUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profileImage
  );

  @Operation(summary = "직원 상세 정보 조회", description = "직원 상세 정보를 조회합니다.")
  ResponseEntity<EmployeeDto> getEmployeeById(
      @Parameter(description = "조회할 직원의 식별자 (ID)") Long id
  );
  @Operation(summary = "직원 삭제", description = "직원을 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteEmployee(@Parameter(description = "직원 ID") @PathVariable Long id);

  ResponseEntity<Map<String, Object>> getEmployees(String nameOrEmail, String employeeNumber,
      String departmentName, String position, LocalDate hireDateFrom, LocalDate hireDateTo,
      EmployeeStatus status, Long idAfter, int size, String sortField, String sortDirection);

  @Operation(summary = "직원 목록 조회", description = "직원 목록을 조회합니다.")
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

  @GetMapping
  ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(EmployeeListRequest request);
}