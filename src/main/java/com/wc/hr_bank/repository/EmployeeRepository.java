package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Employee;
import com.wc.hr_bank.entity.EmployeeStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{

  // 이메일 중복 체크
  boolean existsByEmail(String email);

  // 사원 번호로 특정 직원 찾기
  Optional<Employee> findByEmployeeNumber(String employeeNumber);

  // 연도별 사번 생성을 위한 카운트
  long countByJoinedAtBetween(LocalDate start, LocalDate end);

  /**
   * 명세서 파라미터 기반 다중 조건 필터 쿼리
   */
  @Query("SELECT e FROM Employee e " +
      "WHERE (:idAfter IS NULL OR e.id < :idAfter) " +
      "AND (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%) " +
      "AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%) " +
      "AND (:departmentName IS NULL OR e.department.name LIKE %:departmentName%) " +
      "AND (:position IS NULL OR e.jobTitle LIKE %:position%) " +
      "AND (:status IS NULL OR e.status = :status) " +
      "AND (e.joinedAt BETWEEN :startDate AND :endDate)")
  List<Employee> findEmployeesByFilters(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );
}