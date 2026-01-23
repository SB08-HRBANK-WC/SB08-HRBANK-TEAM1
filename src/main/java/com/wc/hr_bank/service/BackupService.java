package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.response.backup.BackupDto;
import jakarta.servlet.http.HttpServletRequest;

public interface BackupService
{
    BackupDto createBackup(HttpServletRequest httpServletRequest);
}
