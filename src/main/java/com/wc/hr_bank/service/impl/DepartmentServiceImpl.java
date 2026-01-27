package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentCursorPageResponse;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import com.wc.hr_bank.entity.Department;
import com.wc.hr_bank.mapper.DepartmentMapper;
import com.wc.hr_bank.repository.DepartmentRepository;
import com.wc.hr_bank.service.DepartmentService;
import java.time.LocalDate;
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

        //기본 정렬 설정
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(0, size, Sort.by(direction, sortField).and(Sort.by(direction, "id")));

        //날짜 커서 안전하게 파싱
        LocalDate dateCursor = null;
        if ("establishedDate".equals(sortField) && cursor != null && !cursor.isBlank()) {
            try {
                //파싱 로직 변경
                if (cursor.contains("T")) {
                    dateCursor = java.time.LocalDate.parse(cursor.split("T")[0]);
                } else {
                    dateCursor = java.time.LocalDate.parse(cursor);
                }
            } catch (Exception e) {
                dateCursor = null;
            }
        }

        //정렬 기준 및 방향에 따른 쿼리 실행
        Page<Department> page;
        boolean isDesc = "desc".equalsIgnoreCase(sortDirection);

        if ("name".equals(sortField)) {
            page = isDesc ? departmentRepository.searchByNameOrderDesc(nameOrDescription, cursor, idAfter, pageable)
                : departmentRepository.searchByNameOrder(nameOrDescription, cursor, idAfter, pageable);
        } else if ("establishedDate".equals(sortField)) {
            page = isDesc ? departmentRepository.searchByDateOrderDesc(nameOrDescription, dateCursor, idAfter, pageable)
                : departmentRepository.searchByDateOrder(nameOrDescription, dateCursor, idAfter, pageable);
        } else {
            // 기본 ID순 정렬
            page = departmentRepository.searchByIdOrder(nameOrDescription, idAfter, pageable);
        }

        //데이터 변환 (Entity -> Dto)
        List<DepartmentDto> content = page.getContent().stream()
            .map(departmentMapper::toDto)
            .toList();

        //다음 페이지를 위한 커서 정보 생성
        String nextCursor = null;
        Long nextIdAfter = null;

        if (!content.isEmpty()) {
            DepartmentDto lastItem = content.get(content.size() - 1);
            nextIdAfter = lastItem.id();
            // 정렬 기준에 따라 다음 커서에 넣어줄 값 결정
            nextCursor = switch (sortField) {
                case "name" -> lastItem.name();
                case "establishedDate" -> lastItem.establishedDate().toString();
                default -> String.valueOf(lastItem.id());
            };
        }
        //DTO 생성 및 반환
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

