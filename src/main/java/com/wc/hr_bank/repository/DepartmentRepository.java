// 1. DepartmentRepository.java
package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}