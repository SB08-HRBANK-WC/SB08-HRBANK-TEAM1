package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.response.backup.BackupDto;
import com.wc.hr_bank.entity.Backup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BackupMapper
{
    @Mapping(target = "fileId", source = "file.id")
    BackupDto toDto(Backup backup);
}
