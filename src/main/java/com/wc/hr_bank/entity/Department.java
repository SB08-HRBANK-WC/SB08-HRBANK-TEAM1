package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder // Builder 패턴 적용
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "departments")
public class Department extends BaseUpdatableEntity
{
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @Column(name = "established_date", nullable = false)
    private LocalDate establishedDate;

    /**
     * 비즈니스 로직: 부서 정보 수정,
     *
     * @param name 수정 할 부서 이름
     * @param description 수정 할 부서 설명
     * @param establishedDate 수정 할 설립일
     */
    public void update(String name, String description, LocalDate establishedDate) {
        this.name = name;
        this.description = description;
        this.establishedDate = establishedDate;
    }
}
