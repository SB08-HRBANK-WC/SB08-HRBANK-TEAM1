package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "change_log_diffs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeLogDiff
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    private ChangeLog changeLog;

    @Column(name = "field_name", nullable = false)
    private String propertyName;

    @Column(name = "before_value")
    private String before;

    @Column(name = "after_value")
    private String after;

    protected ChangeLogDiff(ChangeLog changeLog, String propertyName, String before, String after) {
        this.changeLog = changeLog;
        this.propertyName = propertyName;
        this.before = before;
        this.after = after;
    }
}
