package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.request.employee.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService
{

    /**
    * 신규 직원을 등록
    * @param request 직원 정보 (JSON)
    * @param profileImage 프로필 이미지 파일 (Multipart)
    * @param ipAddress 요청자 IP
    */
    EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, String ipAddress);

}