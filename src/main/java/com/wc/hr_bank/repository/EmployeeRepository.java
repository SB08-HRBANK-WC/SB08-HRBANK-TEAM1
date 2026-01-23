package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{

    boolean existsByEmail(String email);

    boolean existsByEmployeeNumber(String employeeNumber);

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

}