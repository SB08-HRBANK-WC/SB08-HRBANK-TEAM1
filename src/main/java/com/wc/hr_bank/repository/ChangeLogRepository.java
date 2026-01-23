package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>
{
    @Query("SELECT cl FROM change_log cl ORDER BY cl.updated_at DESC LIMIT 1")
    Instant findTopByOrderByUpdatedAtDesc();
}
