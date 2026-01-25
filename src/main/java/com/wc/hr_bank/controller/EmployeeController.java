package com.wc.hr_bank.controller;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Employee", description = "직원 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController
{

    private final EmployeeService employeeService;

    @Operation(summary = "직원 등록", description = "새로운 직원을 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE) // produces 추가
    public ResponseEntity<EmployeeDto> createEmployee(
        @Parameter(description = "직원 등록 정보 (JSON)", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) // Swagger 설정 명시
        @Valid @RequestPart("employee") EmployeeCreateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profileImage,
        HttpServletRequest servletRequest
    ) {
        String ipAddress = getClientIp(servletRequest);
        EmployeeDto result = employeeService.createEmployee(request, profileImage, ipAddress);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
      }

    private String getClientIp(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
        {
          ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
        {
          ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
        {
          ip = request.getRemoteAddr();
        }

        return ip;
    }

}