package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.response.backup.BackupDto;
import com.wc.hr_bank.entity.Backup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BackupMapper
{
    BackupDto toDto(Backup backup);
}
