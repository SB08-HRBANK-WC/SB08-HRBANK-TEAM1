package com.wc.hr_bank.storage.impl;

import com.wc.hr_bank.global.config.FileConfig;
import com.wc.hr_bank.storage.FileStorage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileStorageImpl implements FileStorage
{

  private final FileConfig fileConfig;
  // 허용할 이미지 파일 종류
  private final List<String> allowedExtensions = new ArrayList<>(List.of(".jpg", ".png", ".jpeg"));

  @Override
  public OutputStream save(Long id, String extension) {
    Path targetPath = resolvePath(id, extension);

    try {
      return Files.newOutputStream(targetPath);
    } catch (IOException e) {
      throw new RuntimeException("파일 쓰기 스트림 생성 실패: " + targetPath, e);
    }
  }

  @Override
  public InputStream get(Long id, String extension) {
    Path targetPath = resolvePath(id, extension);

    try {
      return Files.newInputStream(targetPath);
    } catch (IOException e) {
      throw new RuntimeException("파일 읽기 스트림 생성 실패: " + targetPath, e);
    }
  }

  /**
   * 확장자에 따라 디렉토리 경로를 결정하는 공통 로직
   */
  private Path resolvePath(Long id, String extension) {
    String lowerExt = extension.toLowerCase();
    String fileName = id + lowerExt;

    if (allowedExtensions.contains(lowerExt)) {
      // 이미지 확장자 검증
      return fileConfig.getProfilePath().resolve(fileName);
    } else if (lowerExt.equals(".csv")) {
      // .csv 파일 확장자 검증
      return fileConfig.getBackupPath().resolve(fileName);
    } else if (lowerExt.equals(".log")) {
      // .log 파일 확장자 검증
      return fileConfig.getLogPath().resolve(fileName);
    } else {
      throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + extension);
    }
  }
}
