package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface BackupRepository extends JpaRepository<Backup, Long>
{
    @Query("SELECT dbh FROM db_backup_history dbh ORDER BY dbh.endedAt DESC LIMIT 1")
    Instant findTopByOrderByEndedAtDesc();
}
