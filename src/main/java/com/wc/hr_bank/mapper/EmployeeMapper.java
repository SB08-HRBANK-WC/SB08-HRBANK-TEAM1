package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.response.employee.EmployeeDistDto;
import com.wc.hr_bank.dto.response.employee.EmployeeDto;
import com.wc.hr_bank.dto.response.employee.EmployeeTrendDto;
import com.wc.hr_bank.entity.Employee;
import java.time.LocalDate;
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
  @Mapping(target = "profileImageId", source = "profileImage.id")
  // 1. Enum 타입 필드에는 Enum 객체 자체를 매핑 (빌드 에러 방지)
  @Mapping(target = "status", source = "status")

  EmployeeDto toDto(Employee employee);

  EmployeeDistDto toEmployeeDistDto(String groupKey, Long count, Double percentage);

  EmployeeTrendDto toEmployeeTrendDto(LocalDate date, Long count, Long change, Double changeRate);
}