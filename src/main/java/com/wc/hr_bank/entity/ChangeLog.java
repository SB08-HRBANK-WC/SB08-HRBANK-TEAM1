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

    @Column(name = "target_name", nullable = false, length = 50)
    private String targetName;

    @Column(name = "employee_num", nullable = false, length = 50)
    private String employeeNum;

    @Enumerated(EnumType.STRING)
    private ChangeType type; // CREATED, UPDATED, DELETED

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    // 1:N 관계
    @OneToMany(mappedBy = "changeLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangeLogDiff> diffs = new ArrayList<>();

  public static Object builder() {
    return null;
  }

  public void addDiff(String fieldName, String before, String after) {
        ChangeLogDiff diff = new ChangeLogDiff(this, fieldName, before, after);
        this.diffs.add(diff);
    }
}
