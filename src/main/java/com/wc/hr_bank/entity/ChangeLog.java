package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "change_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeLog extends BaseUpdatableEntity
{
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "employee_name", nullable = false, length = 50)
    private String employeeName;

    @Column(name = "employee_number", nullable = false, length = 50)
    private String employeeNumber;

    @Column(name = "memo", length = 255)
    private String memo;

    @Enumerated(EnumType.STRING)
    private ChangeType type; // CREATED, UPDATED, DELETED

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    // 1:N 관계
    @OneToMany(mappedBy = "changeLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangeLogDiff> diffs = new ArrayList<>();

    public void addDiff(String propertyName, String before, String after) {
        ChangeLogDiff diff = new ChangeLogDiff(this, propertyName, before, after);
        this.diffs.add(diff);
    }
}
