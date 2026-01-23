package com.wc.hr_bank.controller.api;

import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "직원 정보 수정 이력 관리", description = "직원 정보 수정 이력 관리 API")
public interface ChangeLogApi {

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
}
