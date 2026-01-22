package com.wc.hr_bank.entity;

import lombok.Getter;

@Getter
public enum EmployeeStatus
{
    EMPLOYED("재직중"),
    ON_LEAVE("휴직중"),
    RESIGNED("퇴사");

    private final String description;

    EmployeeStatus(String description) {
        this.description = description;
    }
}