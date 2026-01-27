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
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  // 이메일 중복 체크
  boolean existsByEmail(String email);

  // 사원 번호로 특정 직원 찾기
  Optional<Employee> findByEmployeeNumber(String employeeNumber);

  // 연도별 사번 생성을 위한 카운트 (hireDate 기반)
  long countByHireDateBetween(LocalDate start, LocalDate end);

  /**
   * 1. 이름(Name) 기준 정렬 및 커서 기반 페이지네이션
   * 동명이인 대응을 위해 id를 보조 커서로 사용합니다.
   */
  @Query("""
        SELECT e FROM Employee e JOIN FETCH e.department d
        WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
        AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%)
        AND (:departmentName IS NULL OR d.name LIKE %:departmentName%)
        AND (:position IS NULL OR e.position LIKE %:position%)
        AND (:status IS NULL OR e.status = :status)
        AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom)
        AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)
        AND (:cursorName IS NULL OR (e.name > :cursorName OR (e.name = :cursorName AND e.id > :idAfter)))
        ORDER BY e.name ASC, e.id ASC
        """)
  List<Employee> findByNameWithPagingASC(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("cursorName") String cursorName,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );
  @Query("""
        SELECT e FROM Employee e JOIN FETCH e.department d
        WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
        AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%)
        AND (:departmentName IS NULL OR d.name LIKE %:departmentName%)
        AND (:position IS NULL OR e.position LIKE %:position%)
        AND (:status IS NULL OR e.status = :status)
        AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom)
        AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)
        AND (:cursorName IS NULL OR (e.name < :cursorName OR (e.name = :cursorName AND e.id < :idAfter)))
        ORDER BY e.name DESC, e.id DESC
        """)
  List<Employee> findByNameWithPagingDESC(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("cursorName") String cursorName,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );

  /**
   * 2. 입사일(HireDate) 기준 정렬 및 커서 기반 페이지네이션
   * 동일 입사일 대응을 위해 id를 보조 커서로 사용합니다.
   */
  @Query("""
        SELECT e FROM Employee e JOIN FETCH e.department d
        WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
        AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%)
        AND (:departmentName IS NULL OR d.name LIKE %:departmentName%)
        AND (:position IS NULL OR e.position LIKE %:position%)
        AND (:status IS NULL OR e.status = :status)
        AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom)
        AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)
        AND (:cursorDate IS NULL OR (e.hireDate > :cursorDate OR (e.hireDate = :cursorDate AND e.id > :idAfter)))
        ORDER BY e.hireDate ASC, e.id ASC
        """)
  List<Employee> findByHireDateWithPagingASC(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("cursorDate") LocalDate cursorDate,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );

  @Query("""
        SELECT e FROM Employee e JOIN FETCH e.department d
        WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
        AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%)
        AND (:departmentName IS NULL OR d.name LIKE %:departmentName%)
        AND (:position IS NULL OR e.position LIKE %:position%)
        AND (:status IS NULL OR e.status = :status)
        AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom)
        AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)
        AND (:cursorDate IS NULL OR (e.hireDate < :cursorDate OR (e.hireDate = :cursorDate AND e.id < :idAfter)))
        ORDER BY e.hireDate DESC , e.id DESC 
        """)
  List<Employee> findByHireDateWithPagingDESC(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("cursorDate") LocalDate cursorDate,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );

  /**
   * 3. 사원번호(EmployeeNumber) 기준 정렬 및 커서 기반 페이지네이션
   */
  @Query("""
        SELECT e FROM Employee e JOIN FETCH e.department d
        WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
        AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%)
        AND (:departmentName IS NULL OR d.name LIKE %:departmentName%)
        AND (:position IS NULL OR e.position LIKE %:position%)
        AND (:status IS NULL OR e.status = :status)
        AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom)
        AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)
        AND (:cursorNum IS NULL OR (e.employeeNumber > :cursorNum OR (e.employeeNumber = :cursorNum AND e.id > :idAfter)))
        ORDER BY e.employeeNumber ASC, e.id ASC
        """)
  List<Employee> findByEmployeeNumberWithPagingASC(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("cursorNum") String cursorNum,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );

  @Query("""
        SELECT e FROM Employee e JOIN FETCH e.department d
        WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
        AND (:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%)
        AND (:departmentName IS NULL OR d.name LIKE %:departmentName%)
        AND (:position IS NULL OR e.position LIKE %:position%)
        AND (:status IS NULL OR e.status = :status)
        AND (:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom)
        AND (:hireDateTo IS NULL OR e.hireDate <= :hireDateTo)
        AND (:cursorNum IS NULL OR (e.employeeNumber < :cursorNum OR (e.employeeNumber = :cursorNum AND e.id < :idAfter)))
        ORDER BY e.employeeNumber DESC , e.id DESC 
        """)
  List<Employee> findByEmployeeNumberWithPagingDESC(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("status") EmployeeStatus status,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("cursorNum") String cursorNum,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );

  @Query("""
      SELECT
          (CASE
              WHEN :groupBy = 'department' THEN d.name
              ELSE e.position
          END) AS groupKey,
          COUNT(e) AS count,
         (COUNT(e) * 100) / SUM(COUNT(e)) OVER() AS percentage
      FROM Employee e
      JOIN e.department d
      WHERE e.status = :status
      GROUP BY :groupBy
      """)
  List<Object[]> getEmployeesDist(
      String groupBy,
      EmployeeStatus status
  );
}