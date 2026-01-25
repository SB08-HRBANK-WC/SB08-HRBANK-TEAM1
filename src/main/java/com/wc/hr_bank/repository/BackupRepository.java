//package com.wc.hr_bank.repository;
//
//import com.wc.hr_bank.entity.Backup; // 혹은 팀원들이 만든 엔티티 클래스명
//import com.wc.hr_bank.entity.StatusType;
//import io.micrometer.observation.ObservationFilter;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import java.time.Instant;
//
//public interface BackupRepository extends JpaRepository<Backup, Long>
//
//{
//
//  // 수정 전: SELECT dbh FROM db_backup_history dbh ...
//  // 수정 후: db_backup_history를 실제 엔티티 클래스명(예: Backup)으로 변경
//  @Query("SELECT dbh FROM Backup dbh ORDER BY dbh.endedAt DESC LIMIT 1")
//  Backup findTopByOrderByEndedAtDesc();
//
//  ObservationFilter findLatestByStatus(StatusType targetStatus);
//}