package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.ChangeLogApi;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.service.ChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
