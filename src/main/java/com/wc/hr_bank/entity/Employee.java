package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 (외부 접근 차단)
@AllArgsConstructor // Builder 사용을 위해 모든 필드 생성자 추가
@Builder // 객체 생성을 위한 빌더 패턴 적용
public class Employee extends BaseUpdatableEntity
{
    // 생성일, 수정일 공통 클래스 상속
    // 부서와의 연관 관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // 프로필 이미지와의 연관 관계 (1:1, 선택 사항)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File profileImage;

    @Column(name = "employee_number", unique = true, nullable = false, length = 20)
    private String employeeNumber;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "job_title", nullable = false, length = 50)
    private String jobTitle;

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt;

    @Enumerated(EnumType.STRING) // DB에 문자열("EMPLOYED" 등)로 저장
    @Column(name = "status", nullable = false, length = 50)
    private EmployeeStatus status;

    /**
     * 비즈니스 로직: 직원 정보 수정 (사원 번호 제외)
     * 이 메서드 호출 후 EmployeeHistory를 생성하는 로직을 Service에 작성합니다.
     */
    public void updateEmployee(String name, String email, String jobTitle,
                               EmployeeStatus status, Department department, File profileImage) {
        this.name = name;
        this.email = email;
        this.jobTitle = jobTitle;
        this.status = status;
        this.department = department;
        this.profileImage = profileImage;
    }
}