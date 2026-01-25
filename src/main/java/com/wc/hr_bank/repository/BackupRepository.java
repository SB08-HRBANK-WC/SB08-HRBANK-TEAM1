package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Backup;
import com.wc.hr_bank.entity.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface BackupRepository extends JpaRepository<Backup, Long>
{
    @Query(
        """
        SELECT dbh
        FROM db_backup_history dbh
        ORDER BY dbh.endedAt DESC
        LIMIT 1
        """)
    Instant findTopByOrderByEndedAtDesc();

    @Query(
        """
        SELECT dbh
        FROM db_backup_history dbh
        WHERE dbh.status = :status
        ORDER BY dbh.endedAt DESC
        LIMIT 1
        """)
    Optional<Backup> findLatestByStatus(@Param("status") StatusType status);
}
