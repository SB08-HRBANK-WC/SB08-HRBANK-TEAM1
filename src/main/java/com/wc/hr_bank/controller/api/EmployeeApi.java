package com.wc.hr_bank.controller.api;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeListRequest;
import com.wc.hr_bank.dto.request.employee.EmployeeUpdateRequest;
import com.wc.hr_bank.dto.response.employee.CursorPageResponseEmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDistDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeTrendDto;
import com.wc.hr_bank.entity.EmployeeStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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

  /**
   * 대시보드 관련
   */
  @Operation(summary = "직원 분포 조회", description = "직무별, 부서별 직원의 분포도를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = EmployeeDistDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "조회 실패",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      )
  })
  ResponseEntity<List<EmployeeDistDto>> getEmployeesDist(
      @Parameter(
          name = "groupBy",
          description = "그룹화 기준- [department]: 부서별(기본값), [position]: 직무별)",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = String.class, allowableValues = {"department",
              "position"})
      )
      String groupBy,
      @Parameter(
          name = "status",
          description = "직원 상태- 재직중(기본값), 휴직중, 퇴사",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = String.class, allowableValues = {"ACTIVE", "ON_LEAVE",
              "RESIGNED"})
      )
      EmployeeStatus status
  );

  @Operation(summary = "직원 수 조회", description = "지정된 조건에 맞는 직원 수를 조회합니다. 상태 필터링 및 입사일 기간 필터링이 가능합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = Long.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "조회 실패",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      )
  })
  ResponseEntity<Long> getEmployeeCount(
      @Parameter(
          name = "status",
          description = "직원 상태- 재직중(기본값), 휴직중, 퇴사",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = String.class, allowableValues = {"ACTIVE", "ON_LEAVE",
              "RESIGNED"})
      )
      EmployeeStatus status,
      @Parameter(
          name = "fromDate",
          description = "입사일 시작 (지정 시 해당 기간 내 입사한 직원 수 조회, 미지정 시 전체 직원 수 조회)",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = LocalDate.class)
      )
      LocalDate fromDate,
      @Parameter(
          name = "toDate",
          description = "입사일 종료 (fromDate와 함께 사용, 기본값: 현재 일시)",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = LocalDate.class)
      )
      LocalDate toDate
  );

  @Operation(summary = "직원 수 추이 조회", description = "지정된 기간 및 시간 단위로 그룹화된 직원 수 추이를 조회합니다. 파라미터를 제공하지 않으면 최근 12개월 데이터를 월 단위로 반환합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeTrendDto.class)))
      ),
      @ApiResponse(
          responseCode = "404", description = "조회 실패",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      )
  })
  ResponseEntity<List<EmployeeTrendDto>> getEmployeeTrend(
      @Parameter(
          name = "fromDate",
          description = "시작 일시 (기본값: 현재로부터 unit 기준 12개 이전)",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = LocalDate.class)
      )
      LocalDate fromDate,
      @Parameter(
          name = "toDate",
          description = "종료 일시 (기본값: 현재)",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = LocalDate.class)
      )
      LocalDate toDate,
      @Parameter(
          name = "unit",
          description = "시간 단위 (day, week, month, quarter, year, 기본값: month)",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = String.class)
      )
      String unit
  );
}