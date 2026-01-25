package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void addDiff(LogPropertyType property, String before, String after) {
        ChangeLogDiff diff = new ChangeLogDiff(this, property.getLabel(), before, after);
        this.diffs.add(diff);
    }

    public static ChangeLog create(
        Long targetId,
        String employeeName,
        String employeeNumber,
        String memo,
        ChangeType type,
        String ipAddress
    ) {
        ChangeLog log = new ChangeLog();
        log.targetId = targetId;
        log.employeeName = employeeName;
        log.employeeNumber = employeeNumber;
        log.memo = memo;
        log.type = type;
        log.ipAddress = ipAddress;
        return log;
    }
}
