package com.wc.hr_bank.dto.request.changelog;

import com.wc.hr_bank.entity.ChangeType;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public record EmployeeLogRequest
    (
      String employeeNumber,
      ChangeType type,
      String memo,
      String ipAddress,

      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime atFrom,

      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
      LocalDateTime atTo,

      Long idAfter,
      String cursor,
      Integer size,
      String sortField,
      String sortDirection
    )
{
  public EmployeeLogRequest {
    size = (size == null || size <= 0) ? 10 : size;
    sortField = (sortField == null || sortField.isEmpty()) ? "at" : sortField;
    sortDirection = (sortDirection == null || sortDirection.isEmpty()) ? "desc" : sortDirection;
  }
}
