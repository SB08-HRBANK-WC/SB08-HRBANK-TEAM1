package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
  public Employee updateEmployee(String name, String email, String position,
      EmployeeStatus status, Department department, File profileImage, LocalDate hireDate)

  {
    Employee employee = new Employee();
    employee.name = name;
    employee.email = email;
    employee.position = position;
    employee.status = status;
    employee.department = department;
    employee.profileImage = profileImage;
    employee.hireDate = hireDate;

    this.name = name;
    this.email = email;
    this.position = position;
    this.status = status;
    this.department = department;
    this.profileImage = profileImage;
    this.hireDate = hireDate;

    return employee;
  }

}