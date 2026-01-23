package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.response.file.FileDto;
import com.wc.hr_bank.mapper.FileMapper;
import com.wc.hr_bank.repository.FileRepository;
import com.wc.hr_bank.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService
{
  private final FileMapper fileMapper;
  private final FileRepository fileRepository;

  @Override
  public FileDto fileDownload(Long fileId) {
    return null;
  }
}
