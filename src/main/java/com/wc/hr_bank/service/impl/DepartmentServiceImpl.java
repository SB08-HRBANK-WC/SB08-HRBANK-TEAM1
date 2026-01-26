package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentCursorPageResponse;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import com.wc.hr_bank.entity.Department;
import com.wc.hr_bank.mapper.DepartmentMapper;
import com.wc.hr_bank.repository.DepartmentRepository;
import com.wc.hr_bank.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Transactional(readOnly = true)
    public DepartmentCursorPageResponse getAllDepartments(
        String nameOrDescription,
        Long idAfter,
        String cursor,
        int size,
        String sortField,
        String sortDirection) {

        // 1. 커서 처리 (프론트의 cursor 문자열을 Long targetId로 변환)
        Long targetId = idAfter;
        if (targetId == null && cursor != null && !cursor.isBlank()) {
            try {
                targetId = Long.parseLong(cursor);
            } catch (NumberFormatException e) {
                // 유효하지 않은 커서 형식일 경우 첫 페이지 조회(null 유지)
            }
        }

        //정렬 설정: Swagger 명세에 맞춰 ASC와 establishedDate를 기본값으로 설정
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        // 기본 정렬 필드를 establishedDate로 변경
        String field = (sortField == null || sortField.isBlank()) ? "establishedDate" : sortField;

        //다음 페이지 여부 확인을 위해 size + 1을 조회
        Pageable pageable = PageRequest.of(0, size + 1, Sort.by(direction, field));

        //데이터 조회 및 DTO 변환
        String keyword = (nameOrDescription == null) ? "" : nameOrDescription;
        List<DepartmentDto> allContent = departmentRepository.searchByKeyword(keyword, targetId, pageable)
            .stream()
            .map(departmentMapper::toDto)
            .toList();

        // 4. 다음 페이지 판별 및 데이터 절삭
        boolean hasNext = allContent.size() > size;
        List<DepartmentDto> content = hasNext ? allContent.subList(0, size) : allContent;

        // 5. 응답용 커서 정보 생성
        String nextCursor = null;
        Long nextIdAfter = null;
        if (!content.isEmpty()) {
            DepartmentDto lastItem = content.get(content.size() - 1);
            nextCursor = String.valueOf(lastItem.id());
            nextIdAfter = lastItem.id();
        }

        // 6. 전체 개수 조회 (팀 공통 규격)
        long totalElements = departmentRepository.count();

        // 7. 최종 결과 반환
        return new DepartmentCursorPageResponse(
            content,
            nextCursor,
            nextIdAfter,
            size,
            totalElements,
            hasNext
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
