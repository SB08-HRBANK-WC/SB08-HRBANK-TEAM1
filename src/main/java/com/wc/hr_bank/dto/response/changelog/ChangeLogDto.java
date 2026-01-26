package com.wc.hr_bank.dto.response.changelog;

import com.wc.hr_bank.entity.ChangeType;

public record ChangeLogDto
(
  Long id,
  ChangeType type,
  String employeeNumber,
  String memo,
  String ipAddress,
  String at
)
{
}
