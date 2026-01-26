package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Employee extends BaseUpdatableEntity

{

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "file_id")
  private File profileImage;

  @Column(name = "employee_number", unique = true, nullable = false, length = 20)
  private String employeeNumber;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;


  @Column(name = "job_title", nullable = false, length = 50)
  private String position;


  @Column(name = "joined_at", nullable = false)
  private LocalDate hireDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 50)
  private EmployeeStatus status;

  /**
   * 비즈니스 로직: 직원 정보 수정 (사원 번호 제외)
   */
  public void updateEmployee(String name, String email, String position,
      EmployeeStatus status, Department department, File profileImage)

  {
    this.name = name;
    this.email = email;
    this.position = position;
    this.status = status;
    this.department = department;
    this.profileImage = profileImage;
  }

}