package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * department entity repository 인터페이스,
 *
 */
public interface DepartmentRepository extends JpaRepository<Department, Long>
{
}
