package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long>
{
}
