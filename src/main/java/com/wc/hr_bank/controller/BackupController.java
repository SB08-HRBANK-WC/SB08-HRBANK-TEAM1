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
            String worker, StatusType statusType,
            Instant startedAtFrom, Instant startedAtTo,
            Long idAfter, int size,
            String sortField, String sortDirection, String cursor)
    {
        BackupCursorPageRequest backupCursorPageRequest = new BackupCursorPageRequest(
                worker, statusType, startedAtFrom, startedAtTo, idAfter, size, sortField, sortDirection, cursor
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
