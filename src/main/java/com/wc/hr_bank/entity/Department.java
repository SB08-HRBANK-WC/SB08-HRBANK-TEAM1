package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import io.micrometer.observation.annotation.Observed;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "departments")
public class Department extends BaseUpdatableEntity
{
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "established_date", nullable = false)
    private LocalDate establishedDate;

    public Department(String name, String description, LocalDate establishedDate) {
        this.name = name;
        this.description = description;
        this.establishedDate = establishedDate;
    }

  public static Observed builder() {
    return null;
  }

  public void update(String name, String description, LocalDate establishedDate) {
        this.name = name;
        this.description = description;
        this.establishedDate = establishedDate;
    }
}
