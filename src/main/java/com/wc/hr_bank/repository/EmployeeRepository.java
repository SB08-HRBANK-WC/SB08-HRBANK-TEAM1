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

  // 연도별 사번 생성을 위한 카운트 (hireDate로 수정 완료)
  long countByHireDateBetween(LocalDate start, LocalDate end);

  /**
   * 명세서 파라미터 기반 다중 조건 필터 쿼리
   * 🛠️ 수정 포인트: 파라미터 이름을 ServiceImpl과 동일하게 hireDateFrom/To로 맞춤
   */
  @Query("SELECT e FROM Employee e " +
      "WHERE (:idAfter IS NULL OR e.id > :idAfter) " + // 커서 기반 조회를 위해 > 사용
      "AND (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%) " +
      "AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%) " +
      "AND (:departmentName IS NULL OR e.department.name LIKE %:departmentName%) " +
      "AND (:position IS NULL OR e.position LIKE %:position%) " +
      "AND (:status IS NULL OR e.status = :status) " +
      "AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom) " +
      "AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)")
  List<Employee> findEmployeesByFilters(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );

}