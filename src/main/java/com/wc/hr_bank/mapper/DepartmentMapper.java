package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.request.department.DepartmentRequest;
import com.wc.hr_bank.dto.response.department.DepartmentDto;
import com.wc.hr_bank.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

/**
 * department entity와 dto간 데이터 변화 담당 mapper 인터페이스,
 *
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper
{
    /**
     * department 생성 request Dto를 department entity 변환
     *
     * @param request 부서 이름, 설명, 설립일
     * @return DB 저장에 사용할 entity 객체
     */
    Department toEntity(DepartmentRequest request);

        /**
     * department entity를 응답용 dto로 변환하는 메서드,
     *
     * @param department department entity 객체
     * @return 변환된 department dto 객체
     */
    DepartmentDto toDto(Department department);
}
