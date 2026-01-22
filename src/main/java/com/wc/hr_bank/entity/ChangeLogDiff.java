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
public class ChangeLogDiff extends BaseEntity
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id", nullable = false)
    private ChangeLog changeLog;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "before_value")
    private String beforeValue;

    @Column(name = "after_value")
    private String afterValue;

    protected ChangeLogDiff(ChangeLog changeLog, String fieldName, String before, String after) {
        this.changeLog = changeLog;
        this.fieldName = fieldName;
        this.beforeValue = before;
        this.afterValue = after;
    }
}
