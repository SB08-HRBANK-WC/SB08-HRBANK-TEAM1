package com.wc.hr_bank.controller.api;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "직원 관리", description = "직원 관리 API")
public interface EmployeeApi
{

  @Operation(summary = "직원 등록", description = "새로운 직원을 등록합니다.")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)
      )
  )
  ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(description = "등록할 직원의 상세 정보 (JSON)") @RequestPart("request") EmployeeCreateRequest request,
      @Parameter(description = "직원의 프로필 이미지 파일 (Multipart)") @RequestPart(value = "profileImage", required = false)
      MultipartFile profileImage,
      HttpServletRequest servletRequest
  );
  /**
   * 이미지 가이드 반영: summary(제목)와 description(상세 설명) 추가
   */
  @Operation(summary = "직원 수정", description = "직원 정보를 수정합니다.")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
          encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)
      )
  )
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @Parameter(description = "수정할 직원의 상세 정보 (JSON)") @RequestPart("request") EmployeeUpdateRequest request,
      @Parameter(description = "직원의 프로필 이미지 파일 (Multipart)") @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
      HttpServletRequest servletRequest
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
  ResponseEntity<Void> deleteEmployee(
      @Parameter(description = "직원 ID") @PathVariable Long id, HttpServletRequest servletRequest);

  @Operation(summary = "직원 목록 조회", description = "직원 목록을 조회합니다.")
  ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(EmployeeListRequest request);
}