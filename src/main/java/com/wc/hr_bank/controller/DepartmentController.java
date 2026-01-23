package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.DepartmentApi;
import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import com.wc.hr_bank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController implements DepartmentApi
{
    private final DepartmentService departmentService;
    /**
     * 신규 부서 생성,
     *
     */
    @Override
    public DepartmentDto createDepartment(@RequestBody DepartmentRequest request) {
        return departmentService.createDepartment(request);
    }

    /**
     * 모든 부서 목록 조회,
     *
     */
    @Override
    public List<DepartmentDto> getDepartments() {
        return departmentService.getAllDepartments();
    }

    /**
     *  id로 상세 조회,
     *
     */
    @Override
    public DepartmentDto getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }
}
