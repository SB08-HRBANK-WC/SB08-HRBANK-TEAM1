package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.ChangeLog;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>
{
  @EntityGraph(attributePaths = {"diffs"})
  Optional<ChangeLog> findWithDiffsById(Long id);

  @Query("SELECT cl FROM change_log cl ORDER BY cl.updated_at DESC LIMIT 1")
  Instant findTopByOrderByUpdatedAtDesc();

  @Query("SELECT COUNT(c) FROM ChangeLog c " +
  "WHERE c.createdAt >= :start AND c.createdAt <= :end")
  Long countLogsByPeriod(
      @Param("start") Instant start,
      @Param("end") Instant end
  );
}
