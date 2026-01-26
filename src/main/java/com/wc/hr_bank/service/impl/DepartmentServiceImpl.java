package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentCursorPageResponse;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import com.wc.hr_bank.entity.Department;
import com.wc.hr_bank.mapper.DepartmentMapper;
import com.wc.hr_bank.repository.DepartmentRepository;
import com.wc.hr_bank.service.DepartmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public DepartmentCursorPageResponse getAllDepartments(
        String nameOrDescription,
        Long idAfter,
        String cursor,
        int size,
        String sortField,
        String sortDirection) {

        // 1. 교통정리 (기존 로직 유지)
        Long effectiveIdAfter = idAfter;
        if (effectiveIdAfter == null && cursor != null && !cursor.isBlank()) {
            try {
                effectiveIdAfter = Long.parseLong(cursor);
            } catch (NumberFormatException e) {
                effectiveIdAfter = null;
            }
        }

        // 2. 페이징 설정
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "id"));

        // 3. 리포지토리 조회 (Page 객체로 받기)
        Page<Department> page = departmentRepository.searchByKeyword(nameOrDescription, effectiveIdAfter, pageable);

        // 4. 컨텐츠 변환 (Entity -> Dto)
        List<DepartmentDto> content = page.getContent().stream()
            .map(departmentMapper::toDto)
            .toList();

        // 5. 다음 페이지를 위한 커서/ID 계산
        // 데이터가 있으면 마지막 데이터의 ID를 가져오고, 없으면 null
        Long nextIdAfter = content.isEmpty() ? null : content.get(content.size() - 1).id();
        String nextCursor = (nextIdAfter != null) ? String.valueOf(nextIdAfter) : null;

        // 6. 팀 규격 DTO 생성 및 반환
        return new DepartmentCursorPageResponse(
            content,
            nextCursor,
            nextIdAfter,
            page.getSize(),
            page.getTotalElements(),
            page.hasNext() // 다음 페이지 존재 여부
        );
    }

    /**
     * 특정 부서 상세 조회 메서드,
     * @param id 조회할 부서의 식별 번호
     * @return 조회된 부서의 정보가 담긴 dto
     * @throws IllegalArgumentException 해당 id를 가진 부서가 없으면 예외 발생
     *
     */
    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 부서를 찾을 수 없습니다." ));
    }

    /**
     * 부서 수정 메서드,
     *
     * @param id 수정할 부서의 식별 번호
     * @param request 수정할 부서의 정보가 담긴 dto(이름, 설명, 설립일)
     * @return 수정 완료된 부서의 정보
     * @throws IllegalArgumentException 해당 id를 가진 부서가 없으면 예외 발생
     */
    @Override
    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 부서를 찾을 수 업습니다."));

        //중복 체크 로직
        if (!department.getName().equals(request.name())) {
            if (departmentRepository.existsByName(request.name())) {
                throw new IllegalArgumentException("이미 사용 중인 부서 이름입니다: " + request.name());
            }
        }

        department.update(
            request.name(),
            request.description(),
            request.establishedDate()
        );

        return departmentMapper.toDto(department);
    }

    /**
     * 부서 삭제 메서드,
     *
     * @param id 삭제할 부서의 식별 번호
     * @throws IllegalArgumentException 해당 id를 가진 부서가 없으면 예외 발생
     */
    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("삭제하려는 부서가 존재하지 않습니다."));

        //삭제할 부서에 직원이 존재하면 삭제 불가 로직 추가
        if (!department.getEmployees().isEmpty()) {
            throw new IllegalArgumentException("부서에 소속된 직원이 있어 삭제할 수 없습니다.");
        }

        departmentRepository.delete(department);
    }
}
