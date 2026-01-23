package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import com.wc.hr_bank.entity.Department;
import com.wc.hr_bank.mapper.DepartmentMapper;
import com.wc.hr_bank.repository.DepartmentRepository;
import com.wc.hr_bank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService
{
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    /**
     * 새로운 department 생성 메서드,
     *
     * @param request 부서 생성을 위한 데이터 (이름, 설명, 설립일)
     * @return 생성 완료된 부서 정부 (ID, 이름, 설명)
     */
    @Override
    @Transactional
    public DepartmentDto createDepartment(DepartmentRequest request) {
        Department department = departmentMapper.toEntity(request);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    /**
     * 모든 department 정보 조회 반환 메서드,
     *
     * @return  department dto 리스트
     */
    @Override
    @Transactional
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toDto)
                .toList();
    }

    /**
     * 특정 부서 상세 조회 메서드,
     * @param id 조회할 부서의 식별 번호
     * @return 조회된 부서의 정보가 담긴 dto
     * @throws RuntimeException 해당 id를 가진 부서가 없으면 예외 발생
     *
     */
    @Override
    @Transactional
    public DepartmentDto getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new RuntimeException("해당 ID의 부서를 찾을 수 없습니다. ID: " + id));
    }
}
