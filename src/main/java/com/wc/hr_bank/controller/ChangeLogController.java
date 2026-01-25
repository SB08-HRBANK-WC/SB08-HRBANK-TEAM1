package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.ChangeLogApi;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.service.ChangeLogService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController implements ChangeLogApi
{
  private final ChangeLogService changeLogService;

  @Override
  public ResponseEntity<ChangeLogDetailDto> getChangeLogDiffs(@PathVariable Long id) {
    ChangeLogDetailDto response = changeLogService.getChangeLogDetail(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getChangeLogCount(
      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,

      @RequestParam(required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to)
  {
    LocalDateTime actualFrom = (from != null) ? from : LocalDateTime.now().minusDays(7);
    LocalDateTime actualTo = (to != null) ? to : LocalDateTime.now();

    Long count = changeLogService.countByPeriod(actualFrom, actualTo);

    return ResponseEntity.ok(count);
  }

}
