package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.DepartmentApi;
import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import com.wc.hr_bank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.createDepartment(request));
    }

    /**
     * 모든 부서 목록 조회,
     *
     */
    @Override
    public ResponseEntity<Page<DepartmentDto>> getDepartments(
        String nameOrDescription,
        Long idAfter,
        String cursor,
        int size,
        String sortField,
        String sortDirection
    ) {
        Page<DepartmentDto> response = departmentService.getAllDepartments(
            nameOrDescription, idAfter, cursor, size, sortField, sortDirection);
        return ResponseEntity.ok(response);
    }

    /**
     *  id로 상세 조회,
     *
     */
    @Override
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    /**
     * 부서 수정,
     *
     */
    @Override
    public ResponseEntity<DepartmentDto> updateDepartment(
        @PathVariable Long id,
        @RequestBody DepartmentRequest request) {

        DepartmentDto updated = departmentService.updateDepartment(id, request);

        return ResponseEntity.ok(updated);
    }

    /**
     * 부서 삭제,
     *
     */
    @Override
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
