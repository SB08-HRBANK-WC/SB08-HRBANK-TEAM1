package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentCursorPageResponse;
import com.wc.hr_bank.dto.response.department.DepartmentDto;

public interface DepartmentService
{
    /**
     * 신규 부서 생성,
     *
     * @param request 부서 생성을 위한 데이터 (이름, 설명, 설립일)
     * @return 생성 완료된 부서 정보 반환
     */
    DepartmentDto createDepartment(DepartmentRequest request);

    /**
     * 모든 부서 목록 조회,
     *
     */
    DepartmentCursorPageResponse getAllDepartments(
        String nameOrDescription,
        Long idAfter,
        String cursor,
        int size,
        String sortField,
        String sortDirection
    );

    /**
     * 특정 부서 상세 조회,
     *
     */
    DepartmentDto getDepartmentById(Long id);

    /**
     * 부서 수정,
     *
     */
    DepartmentDto updateDepartment(Long id, DepartmentRequest request);

    /**
     * 부서 삭제,
     *
     */
    void deleteDepartment(Long id);
}
