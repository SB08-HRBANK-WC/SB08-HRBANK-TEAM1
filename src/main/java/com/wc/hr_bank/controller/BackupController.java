package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.BackupApi;
import com.wc.hr_bank.dto.request.backup.BackupCursorPageRequest;
import com.wc.hr_bank.dto.response.backup.BackupCursorPageResponseDto;
import com.wc.hr_bank.dto.response.backup.BackupDto;
import com.wc.hr_bank.entity.StatusType;
import com.wc.hr_bank.service.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/backups")
public class BackupController implements BackupApi
{
    private final BackupService backupService;

    @PostMapping
    public ResponseEntity<BackupDto> create(HttpServletRequest httpServletRequest) {
        BackupDto backupDto = backupService.createBackup(httpServletRequest);

        return ResponseEntity.ok(backupDto);
    }

    @GetMapping
    public ResponseEntity<BackupCursorPageResponseDto> getAllBackups(
            String worker, StatusType status, String startedAtFrom, String startedAtTo, Long idAfter,
            int size, String sortField, String sortDirection, String cursor)
    {
        Instant from = null;
        Instant to = null;

        try {
            if (startedAtFrom != null && !startedAtFrom.isBlank()) {
                Instant parsedFrom = Instant.parse(startedAtFrom);

                LocalDateTime kstDateTime = LocalDateTime.ofInstant(parsedFrom, ZoneId.of("Asia/Seoul"));
                System.out.println("from을 KST로 변환: " + kstDateTime);

                if (kstDateTime.getHour() == 0 && kstDateTime.getMinute() == 0 && kstDateTime.getSecond() == 0) {
                    LocalDate targetDate = kstDateTime.toLocalDate();
                    from = targetDate.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();
                    System.out.println("→ from: " + targetDate + " 00:00:00 KST = " + from + " UTC");
                } else {
                    from = parsedFrom;
                    System.out.println("→ from (그대로): " + from);
                }
            }

            if (startedAtTo != null && !startedAtTo.isBlank()) {
                Instant parsedTo = Instant.parse(startedAtTo);

                LocalDateTime kstDateTime = LocalDateTime.ofInstant(parsedTo, ZoneId.of("Asia/Seoul"));
                System.out.println("to를 KST로 변환: " + kstDateTime);

                if (kstDateTime.getHour() == 0 && kstDateTime.getMinute() == 0 && kstDateTime.getSecond() == 0) {
                    LocalDate targetDate = kstDateTime.toLocalDate();
                    to = targetDate.atTime(23, 59, 59, 999_999_999)
                            .atZone(ZoneId.of("Asia/Seoul"))
                            .toInstant();
                    System.out.println("→ to: " + targetDate + " 23:59:59 KST = " + to + " UTC");
                } else {
                    to = parsedTo;
                }
            }

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

        BackupCursorPageRequest backupCursorPageRequest = new BackupCursorPageRequest(
                worker, status, from, to, idAfter, size, sortField, sortDirection, cursor
        );

        BackupCursorPageResponseDto responseDto = backupService.findAll(backupCursorPageRequest);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/latest")
    public ResponseEntity<BackupDto> getLatestBackup(StatusType statusType) {
        BackupDto backupDto = backupService.getLatest(statusType);

        return ResponseEntity.ok(backupDto);
    }
}
