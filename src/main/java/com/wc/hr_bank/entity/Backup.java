package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "db_backup_history")
@Entity
public class Backup extends BaseEntity
{
    @Column(name = "worker", nullable = false, length = 50)
    private String worker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusType status;

    @Column(name = "started_at", nullable = false)
    private Instant started_at;

    @Column(name = "ended_at")
    private Instant ended_at;

    @OneToOne(fetch = FetchType.LAZY)
    private File file;

    public Backup(String worker, StatusType status, Instant started_at, Instant ended_at) {
        this.worker = worker;
        this.status = status;
        this.started_at = started_at;
        this.ended_at = ended_at;
    }

    public void update(Instant ended_at, File file, StatusType status) {
        if (ended_at != null) {
            this.ended_at = ended_at;
        }
        if (file != null) {
            this.file = file;
        }
        this.status = status;
    }
}