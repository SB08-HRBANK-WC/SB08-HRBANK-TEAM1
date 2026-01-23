package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.request.file.EmployeeCreateRequest;
import com.wc.hr_bank.dto.response.file.EmployeeDto;
import com.wc.hr_bank.entity.*;
import com.wc.hr_bank.repository.DepartmentRepository;
import com.wc.hr_bank.repository.EmployeeRepository;
import com.wc.hr_bank.repository.FileRepository;
import com.wc.hr_bank.repository.ChangeLogRepository;
import com.wc.hr_bank.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService
{

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final FileRepository fileRepository;
    private final ChangeLogRepository changeLogRepository;

    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage, String ipAddress) {
      // 1. 이메일 중복 검증 (명세서 400 대응)
      if (employeeRepository.existsByEmail(request.getEmail()))
      {
        throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
      }

      // 2. 부서 조회 (명세서 404 대응을 위해 실제 ID로 조회)
      // 만약 테스트 중이라 부서 데이터가 없다면 이전처럼 가짜 부서를 생성하는 로직을 유지해도 됩니다.
      Department department = departmentRepository.findById(request.getDepartmentId())
          .orElseThrow(() -> new IllegalArgumentException("해당 부서를 찾을 수 없습니다."));

      // 3. 프로필 이미지 처리 (파일 관리 기능 연동 대기)
      File savedFile = null;
      if (profileImage != null && !profileImage.isEmpty())
      {
        // TODO: 파일 업로드 서비스 호출 로직
      }

      // 4. 사원 번호 자동 부여 (규칙: 입사연도-순번)
      String employeeNumber = generateEmployeeNumber(request.getHireDate());

      // 5. 직원 엔티티 생성 및 상태 자동 초기화
      Employee employee = Employee.builder()
          .name(request.getName())
          .email(request.getEmail())
          .jobTitle(request.getPosition())
          .joinedAt(request.getHireDate())
          .status(EmployeeStatus.EMPLOYED) // 자동으로 재직중 상태 초기화
          .department(department)
          .profileImage(savedFile)
          .employeeNumber(employeeNumber)
          .build();

      Employee savedEmployee = employeeRepository.save(employee);

      // 6. 관리 이력 등록 (추출된 IP 주소 활용)
      saveRegistrationHistory(savedEmployee, request.getMemo(), ipAddress);

      // 7. 명세서 200 OK 규격에 맞춰 상세 데이터 반환
      return EmployeeDto.builder()
          .id(savedEmployee.getId())
          .name(savedEmployee.getName())
          .email(savedEmployee.getEmail())
          .employeeNumber(savedEmployee.getEmployeeNumber())
          .departmentId(department.getId())
          .departmentName(department.getName())
          .position(savedEmployee.getJobTitle())
          .hireDate(savedEmployee.getJoinedAt())
          .status("재직중")
          .profileImageId(savedFile != null ? savedFile.getId() : null)
          .build();
    }

    private String generateEmployeeNumber(LocalDate hireDate)
    {
      long count = employeeRepository.count() + 1;
      return String.format("%d-%03d", hireDate.getYear(), count);
    }

    private void saveRegistrationHistory(Employee employee, String memo, String ipAddress)
    {
      // 이력 관리 팀원의 ChangeLog 엔티티와 연동하여 ipAddress 저장
    }
}