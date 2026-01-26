package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.Backup;
import com.wc.hr_bank.entity.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 첫 페이지
    @Query(
        """
        SELECT dbh
        FROM db_backup_history dbh
        WHERE (:worker IS NULL OR dbh.worker LIKE :worker)
        AND (:status IS NULL OR dbh.status = :status)
        AND (:startedAtFrom IS NULL OR dbh.startedAt >= :startedAtFrom)
        AND (:startedAtTo IS NULL OR dbh.startedAt <= :startedAtTo)
        """)
    Page<Backup> findAllWithoutCursor(
            @Param("worker") String worker,
            @Param("status") StatusType status,
            @Param("startedAtFrom") Instant startedAtFrom,
            @Param("startedAtTo") Instant startedAtTo,
            Pageable pageable
    );

    // startedAt, ASC
    @Query(
        """
        SELECT dbh
        FROM db_backup_history dbh
        WHERE (:worker IS NULL OR dbh.worker LIKE :worker)
        AND (:status IS NULL OR dbh.status = :status)
        AND (:startedAtFrom IS NULL OR dbh.startedAt >= :startedAtFrom)
        AND (:startedAtTo IS NULL OR dbh.startedAt <= :startedAtTo)
        AND (dbh.startedAt > :cursor
                OR (dbh.startedAt = :cursor AND dbh.id >= :idAfter)
        )
        """)
    Page<Backup> findAllWithCursorOrderByStartedAtAsc(
            @Param("worker") String worker,
            @Param("status") StatusType status,
            @Param("startedAtFrom") Instant startedAtFrom,
            @Param("startedAtTo") Instant startedAtTo,
            @Param("cursor") Instant cursor,
            @Param("idAfter") Long idAfter,
            Pageable pageable
    );

    // startedAt, DESC
    @Query(
            """
            SELECT dbh
            FROM db_backup_history dbh
            WHERE (:worker IS NULL OR dbh.worker LIKE :worker)
            AND (:status IS NULL OR dbh.status = :status)
            AND (:startedAtFrom IS NULL OR dbh.startedAt >= :startedAtFrom)
            AND (:startedAtTo IS NULL OR dbh.startedAt <= :startedAtTo)
            AND (dbh.startedAt < :cursor
                    OR (dbh.startedAt = :cursor AND dbh.id <= :idAfter)
            )
            """)
    Page<Backup> findAllWithCursorOrderByStartedAtDesc(
            @Param("worker") String worker,
            @Param("status") StatusType status,
            @Param("startedAtFrom") Instant startedAtFrom,
            @Param("startedAtTo") Instant startedAtTo,
            @Param("cursor") Instant cursor,
            @Param("idAfter") Long idAfter,
            Pageable pageable
    );

    // endedAt, ASC
    @Query(
        """
        SELECT dbh
        FROM db_backup_history dbh
        WHERE (:worker IS NULL OR dbh.worker LIKE :worker)
        AND (:status IS NULL OR dbh.status = :status)
        AND (:startedAtFrom IS NULL OR dbh.startedAt >= :startedAtFrom)
        AND (:startedAtTo IS NULL OR dbh.startedAt <= :startedAtTo)
        AND (dbh.endedAt > :cursor
                OR (dbh.endedAt = :cursor AND dbh.id >= :idAfter)
        )
        """)
    Page<Backup> findAllWithCursorOrderByEndedAtAsc(
            @Param("worker") String worker,
            @Param("status") StatusType status,
            @Param("startedAtFrom") Instant startedAtFrom,
            @Param("startedAtTo") Instant startedAtTo,
            @Param("cursor") Instant cursor,
            @Param("idAfter") Long idAfter,
            Pageable pageable
    );

    // endedAt, DESC
    @Query(
        """
        SELECT dbh
        FROM db_backup_history dbh
        WHERE (:worker IS NULL OR dbh.worker LIKE :worker)
        AND (:status IS NULL OR dbh.status = :status)
        AND (:startedAtFrom IS NULL OR dbh.startedAt >= :startedAtFrom)
        AND (:startedAtTo IS NULL OR dbh.startedAt <= :startedAtTo)
        AND (dbh.endedAt > :cursor
                OR (dbh.endedAt = :cursor AND dbh.id <= :idAfter)
        )
        """)
    Page<Backup> findAllWithCursorOrderByEndedAtDesc(
            @Param("worker") String worker,
            @Param("status") StatusType status,
            @Param("startedAtFrom") Instant startedAtFrom,
            @Param("startedAtTo") Instant startedAtTo,
            @Param("cursor") Instant cursor,
            @Param("idAfter") Long idAfter,
            Pageable pageable
    );
}
