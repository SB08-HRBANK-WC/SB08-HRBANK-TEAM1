package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.ChangeLog;
import com.wc.hr_bank.entity.ChangeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>
{
    @EntityGraph(attributePaths = {"diffs"})
    Optional<ChangeLog> findWithDiffsById(Long id);

    @Query("SELECT COUNT(c) FROM ChangeLog c " +
            "WHERE c.createdAt >= :start AND c.createdAt <= :end")
    Long countLogsByPeriod(
            @Param("start") Instant start,
            @Param("end") Instant end
    );

    // 수정일(at) 기준 내림차순 (기본 값)
    @Query(value = """
       SELECT l FROM ChangeLog l
       WHERE (:employeeNumber IS NULL OR l.employeeNumber LIKE %:employeeNumber%)
       AND   (:type IS NULL OR l.type = :type)
       AND   (:memo IS NULL OR l.memo LIKE %:memo%)
       AND   (:ipAddress IS NULL OR l.ipAddress LIKE %:ipAddress%)
       AND   (cast(:atFrom as timestamp) IS NULL OR l.createdAt >= :atFrom)
       AND   (cast(:atTo as timestamp) IS NULL OR l.createdAt <= :atTo)
       AND   (cast(:cursorAt as timestamp) IS NULL OR (l.createdAt < :cursorAt OR (l.createdAt = :cursorAt AND l.id < :idAfter)))
      ORDER BY l.createdAt DESC, l.id DESC
      """)
    List<ChangeLog> findByAtDesc(
            @Param("employeeNumber") String employeeNumber,
            @Param("type") ChangeType type,
            @Param("memo") String memo,
            @Param("ipAddress") String ipAddress,
            @Param("atFrom") Instant atFrom,
            @Param("atTo") Instant atTo,
            @Param("cursorAt") Instant cursorAt,
            @Param("idAfter") Long idAfter,
            Pageable pageable);

    // 수정일(at) 기준 오름차순
    @Query("""
       SELECT l FROM ChangeLog l
       WHERE (:employeeNumber IS NULL OR l.employeeNumber LIKE %:employeeNumber%)
       AND   (:type IS NULL OR l.type = :type)
       AND   (:memo IS NULL OR l.memo LIKE %:memo%)
       AND   (:ipAddress IS NULL OR l.ipAddress LIKE %:ipAddress%)
       AND   (cast(:atFrom as timestamp) IS NULL OR l.createdAt >= :atFrom)
       AND   (cast(:atTo as timestamp) IS NULL OR l.createdAt <= :atTo)
       AND   (cast(:cursorAt as timestamp) IS NULL OR (l.createdAt > :cursorAt OR (l.createdAt = :cursorAt AND l.id > :idAfter)))
      ORDER BY l.createdAt ASC, l.id ASC
      """)
    List<ChangeLog> findByAtAsc(
            @Param("employeeNumber") String employeeNumber,
            @Param("type") ChangeType type,
            @Param("memo") String memo,
            @Param("ipAddress") String ipAddress,
            @Param("atFrom") Instant atFrom,
            @Param("atTo") Instant atTo,
            @Param("cursorAt") Instant cursorAt,
            @Param("idAfter") Long idAfter,
            Pageable pageable);

    // IP 주소 기준 내림차순
    @Query("""
       SELECT l FROM ChangeLog l
       WHERE (:employeeNumber IS NULL OR l.employeeNumber LIKE %:employeeNumber%)
       AND   (:type IS NULL OR l.type = :type)
       AND   (:memo IS NULL OR l.memo LIKE %:memo%)
       AND   (:ipAddress IS NULL OR l.ipAddress LIKE %:ipAddress%)
       AND   (cast(:atFrom as timestamp) IS NULL OR l.createdAt >= :atFrom)
       AND   (cast(:atTo as timestamp) IS NULL OR l.createdAt <= :atTo)
       AND (cast(:cursorIp as string) IS NULL OR (l.ipAddress < :cursorIp OR (l.ipAddress = :cursorIp AND l.id < :idAfter)))
      ORDER BY l.ipAddress DESC, l.id DESC
      """)
    List<ChangeLog> findByIpDesc(
            @Param("employeeNumber") String employeeNumber,
            @Param("type") ChangeType type,
            @Param("memo") String memo,
            @Param("ipAddress") String ipAddress,
            @Param("atFrom") Instant atFrom,
            @Param("atTo") Instant atTo,
            @Param("cursorIp") String cursorIp,
            @Param("idAfter") Long idAfter,
            Pageable pageable);

    // IP 주소 기준 오름차순
    @Query("""
       SELECT l FROM ChangeLog l
       WHERE (:employeeNumber IS NULL OR l.employeeNumber LIKE %:employeeNumber%)
       AND   (:type IS NULL OR l.type = :type)
       AND   (:memo IS NULL OR l.memo LIKE %:memo%)
       AND   (:ipAddress IS NULL OR l.ipAddress LIKE %:ipAddress%)
       AND   (cast(:atFrom as timestamp) IS NULL OR l.createdAt >= :atFrom)
       AND   (cast(:atTo as timestamp) IS NULL OR l.createdAt <= :atTo)
       AND (cast(:cursorIp as string) IS NULL OR (l.ipAddress > :cursorIp OR (l.ipAddress = :cursorIp AND l.id > :idAfter)))
      ORDER BY l.ipAddress ASC, l.id ASC
      """)
    List<ChangeLog> findByIpAsc(
            @Param("employeeNumber") String employeeNumber,
            @Param("type") ChangeType type,
            @Param("memo") String memo,
            @Param("ipAddress") String ipAddress,
            @Param("atFrom") Instant atFrom,
            @Param("atTo") Instant atTo,
            @Param("cursorIp") String cursorIp,
            @Param("idAfter") Long idAfter,
            Pageable pageable);

    @Query("""
       SELECT COUNT(l) FROM ChangeLog l
       WHERE (:employeeNumber IS NULL OR l.employeeNumber LIKE %:employeeNumber%)
       AND   (:type IS NULL OR l.type = :type)
       AND   (:memo IS NULL OR l.memo LIKE %:memo%)
       AND   (:ipAddress IS NULL OR l.ipAddress LIKE %:ipAddress%)
       AND   (cast(:atFrom as timestamp) IS NULL OR l.createdAt >= :atFrom)
       AND   (cast(:atTo as timestamp) IS NULL OR l.createdAt <= :atTo)
      """)
    Long countByFilters(
            @Param("employeeNumber") String employeeNumber,
            @Param("type") ChangeType type,
            @Param("memo") String memo,
            @Param("ipAddress") String ipAddress,
            @Param("atFrom") Instant atFrom,
            @Param("atTo") Instant atTo
    );
}