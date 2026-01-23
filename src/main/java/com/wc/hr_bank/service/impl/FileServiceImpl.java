package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.response.file.FileDto;
import com.wc.hr_bank.entity.File;
import com.wc.hr_bank.mapper.FileMapper;
import com.wc.hr_bank.repository.FileRepository;
import com.wc.hr_bank.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService
{
  private final FileMapper fileMapper;
  private final FileRepository fileRepository;

  @Override
  public FileDto fileDownload(Long fileId) {
    // DB 파일 조회
    File findFile = fileRepository.findById(fileId)
        .orElseThrow(() -> new NoSuchElementException("해당 ID로 파일을 찾을 수 없습니다. ID: " + fileId));

    // 조회된 FileEntity로 실제 파일 조회
    Resource resource = getFile(findFile);

    return fileMapper.toFileDto(findFile, resource);
  }

  @Override
  public Resource getFile(File file) {
    try {
      Path path = Paths.get(file.getFilePath());
      Resource resource = new UrlResource(path.toUri());

      if (!resource.exists() || !resource.isReadable()) {
        throw new RuntimeException("파일이 존재하지 않거나 읽을 수 없습니다. FilePath: " + file.getFilePath());
      }

      return resource;
    } catch (MalformedURLException e) {
      log.error("파일 경로 오류: {}", e.getMessage());
      throw new RuntimeException("파일 경로가 잘못되었습니다.", e);
    }
  }
}
