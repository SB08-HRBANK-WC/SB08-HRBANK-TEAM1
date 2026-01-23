package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.ChangeLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>
{
  @EntityGraph(attributePaths = {"diffs"})
  Optional<ChangeLog> findWithDiffsById(Long id);
}
