package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct를 이용한 직원 정보 매퍼 인터페이스입니다.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper

{

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "position", source = "jobTitle")
    @Mapping(target = "hireDate", source = "joinedAt")
    @Mapping(target = "status", expression = "java(employee.getStatus() != null ? employee.getStatus().getDescription() : \"재직중\")")
    @Mapping(target = "profileImageId", source = "profileImage.id")
    EmployeeDto toDto(Employee employee);

}