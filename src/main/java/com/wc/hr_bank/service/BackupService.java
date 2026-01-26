package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.request.backup.BackupCursorPageRequest;
import com.wc.hr_bank.dto.response.backup.BackupCursorPageResponseDto;
import com.wc.hr_bank.dto.response.backup.BackupDto;
import com.wc.hr_bank.entity.StatusType;
import jakarta.servlet.http.HttpServletRequest;

public interface BackupService
{
    BackupCursorPageResponseDto findAll(BackupCursorPageRequest backupCursorPageRequest);
    BackupDto getLatest(StatusType statusType);
    BackupDto createBackup(HttpServletRequest httpServletRequest);
    BackupDto batchBackup();
}
