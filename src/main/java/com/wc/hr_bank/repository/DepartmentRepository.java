package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  /**
   *이름 또는 설명 검색 + No-Offset 페이징 + 정렬 및 크기 제한
   */
  @Query(value = "SELECT d FROM Department d " +
      "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
      "AND (:idAfter IS NULL OR d.id > :idAfter)",
      countQuery = "SELECT count(d) FROM Department d " +
          "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
          "AND (:idAfter IS NULL OR d.id > :idAfter)")
  Page<Department> searchByKeyword(
      @Param("keyword") String keyword,
      @Param("idAfter") Long idAfter,
      Pageable pageable
  );
}
