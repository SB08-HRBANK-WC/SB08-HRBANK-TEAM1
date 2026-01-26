package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.ChangeLogApi;
import com.wc.hr_bank.dto.request.changelog.ChangeLogRequest;
import com.wc.hr_bank.dto.response.changelog.ChangeLogDetailDto;
import com.wc.hr_bank.dto.response.changelog.CursorPageResponseChangeLogDto;
import com.wc.hr_bank.service.ChangeLogService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
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

  /**
   * 직원 정보 수정 이력 목록 조회
   * @param request
   * @return
   */
  @Override
  public ResponseEntity<CursorPageResponseChangeLogDto> getEmployeeLogs (@ModelAttribute ChangeLogRequest request) {

    CursorPageResponseChangeLogDto changeLogs = changeLogService.getChangeLogs(request);

    return ResponseEntity.ok(changeLogs);
  }

  /**
   * 직원 정보 수정 이력 상세 조회
   * @param id
   * @return
   */
  @Override
  public ResponseEntity<ChangeLogDetailDto> getChangeLogDiffs(@PathVariable Long id) {

    ChangeLogDetailDto response = changeLogService.getChangeLogDetail(id);

    return ResponseEntity.ok(response);
  }

  // 수정 이력 기간 별 건수 조회
  @Override
  public ResponseEntity<Long> getChangeLogCount(
      @RequestParam(required = false) LocalDateTime from,
      @RequestParam(required = false) LocalDateTime to) {

    LocalDateTime actualFrom = (from != null) ? from : LocalDateTime.now().minusDays(7);
    LocalDateTime actualTo = (to != null) ? to : LocalDateTime.now();

    Long count = changeLogService.countByPeriod(actualFrom, actualTo);

    return ResponseEntity.ok(count);
  }
}
