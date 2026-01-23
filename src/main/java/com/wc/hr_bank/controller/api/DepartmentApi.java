package com.wc.hr_bank.controller.api;

import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "부서 관리", description = "부서 관리 API")
public interface DepartmentApi
{
    @Operation(summary = "부서 등록", description = "새로운 부서를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이름"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    DepartmentDto createDepartment(@RequestBody DepartmentRequest request);

    @Operation(summary = "부서 목록 조회", description = "모든 부서의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    List<DepartmentDto> getDepartments();

    @Operation(summary = "부서 상세 조회", description = "부서 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
            @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    @GetMapping("/{id}")
    DepartmentDto getDepartmentById(
            @Parameter(description = "부서 ID", required = true) @PathVariable Long id
    );


}
