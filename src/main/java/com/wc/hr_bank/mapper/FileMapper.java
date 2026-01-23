package com.wc.hr_bank.mapper;

import com.wc.hr_bank.dto.response.file.FileDto;
import com.wc.hr_bank.entity.File;
import org.mapstruct.Mapper;
import org.springframework.core.io.Resource;

@Mapper(componentModel = "spring")
public interface FileMapper
{
  FileDto toFileDto(File file, Resource resource);
}
