package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * department entity repository 인터페이스,
 *
 */
public interface DepartmentRepository extends JpaRepository<Department, Long>
{
  /**
   * department 중복 이름 체크 메서드,
   *
   */
  boolean existsByName(String name);
}
