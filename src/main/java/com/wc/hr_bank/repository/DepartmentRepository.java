package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Department;
import java.time.LocalDate;
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
//ID순 정렬
    @Query(value = "SELECT d FROM Department d " +
            "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
            "AND (:idAfter IS NULL OR d.id > :idAfter)",
            countQuery = "SELECT count(d) FROM Department d " +
                    "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
                    "AND (:idAfter IS NULL OR d.id > :idAfter)")
    Page<Department> searchByIdOrder(@Param("keyword") String keyword, @Param("idAfter") Long idAfter, Pageable pageable);

    //부서명순 정렬 (ASC)
    @Query(value = "SELECT d FROM Department d " +
            "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
            "AND (:cursor IS NULL OR (d.name > :cursor) OR (d.name = :cursor AND d.id > :idAfter))",
            countQuery = "SELECT count(d) FROM Department d " +
                    "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%)")
    Page<Department> searchByNameOrder(@Param("keyword") String keyword, @Param("cursor") String cursor, @Param("idAfter") Long idAfter, Pageable pageable);

    //부서명순 정렬 (DESC)
    @Query(value = "SELECT d FROM Department d " +
            "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
            "AND (:cursor IS NULL OR d.name < :cursor OR (d.name = :cursor AND d.id < :idAfter))",
            countQuery = "SELECT count(d) FROM Department d " +
                    "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%)")
    Page<Department> searchByNameOrderDesc(@Param("keyword") String keyword, @Param("cursor") String cursor, @Param("idAfter") Long idAfter, Pageable pageable);

    //설립일순 정렬 (ASC)
    @Query(value = "SELECT d FROM Department d " +
            "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
            "AND (CAST(:cursor AS localdate) IS NULL OR (d.establishedDate > :cursor) OR (d.establishedDate = :cursor AND d.id > :idAfter))",
            countQuery = "SELECT count(d) FROM Department d " +
                    "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%)")
    Page<Department> searchByDateOrder(@Param("keyword") String keyword, @Param("cursor") LocalDate cursor, @Param("idAfter") Long idAfter, Pageable pageable);

    //설립일순 정렬 (DESC)
    @Query(value = "SELECT d FROM Department d " +
            "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%) " +
            "AND (CAST(:cursor AS localdate) IS NULL OR d.establishedDate < :cursor OR (d.establishedDate = :cursor AND d.id < :idAfter))",
            countQuery = "SELECT count(d) FROM Department d " +
                    "WHERE (:keyword IS NULL OR d.name LIKE %:keyword% OR d.description LIKE %:keyword%)")
    Page<Department> searchByDateOrderDesc(@Param("keyword") String keyword, @Param("cursor") LocalDate cursor, @Param("idAfter") Long idAfter, Pageable pageable);
}