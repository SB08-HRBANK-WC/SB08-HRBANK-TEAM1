package com.wc.hr_bank.controller.api;

import com.wc.hr_bank.dto.request.changelog.ChangeLogRequest;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.dto.response.changelog.CursorPageResponseChangeLogDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "직원 정보 수정 이력 관리", description = "직원 정보 수정 이력 관리 API")
public interface ChangeLogApi
{

  @Operation(
    summary = "직원 정보 수정 이력 목록 조회",
    description = "직원 정보 수정 이력 목록을 조회합니다. 상세 변경 내용은 포함되지 않습니다.",
      operationId = "getAllChangeLogs"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseChangeLogDto.class))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 또는 지원하지 않는 정렬 필드",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping
  ResponseEntity<CursorPageResponseChangeLogDto> getEmployeeLogs(
      @ModelAttribute ChangeLogRequest request
  );

  @Operation(
      summary = "직원 정보 수정 이력 상세 조회",
      description = "직원 정보 수정 이력의 상세 정보를 조회합니다. 변경 상세 내용이 포함됩니다.",
      operationId = "getChangeLogDiffs"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = ChangeLogDetailDto.class))
      ),
      @ApiResponse(
          responseCode = "404",
          description = "이력을 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))
      )
  })
  @GetMapping("/{id}")
  ResponseEntity<ChangeLogDetailDto> getChangeLogDiffs(
      @PathVariable @Parameter(description = "이력 ID", required = true) Long id
  );

  @Operation(
      summary = "수정 이력 건수 조회",
      description = "직원 정보 수정 이력 건수를 조회합니다. 파라미터를 제공하지 않으면 최근 일주일 데이터를 반환합니다.",
      operationId = "getChangeLogsCount"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "조회 성공",
          content = @Content(schema = @Schema(implementation = Long.class))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 또는 유효하지 않은 날짜 범위",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/count")
  ResponseEntity<Long> getChangeLogCount(
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      @Parameter(description = "시작 일시 (기본값: 7일 전)") LocalDateTime from,

      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      @Parameter(description = "종료 일시 (기본값: 현재)") LocalDateTime to
  );
}