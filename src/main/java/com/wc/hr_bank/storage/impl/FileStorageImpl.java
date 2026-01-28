package com.wc.hr_bank.storage.impl;

import com.wc.hr_bank.entity.File;
import com.wc.hr_bank.global.config.FileConfig;
import com.wc.hr_bank.repository.FileRepository;
import com.wc.hr_bank.storage.FileStorage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileStorageImpl implements FileStorage
{
  // 허용할 이미지 파일 종류.
  private final List<String> allowedImages = new ArrayList<>(
      List.of(".jpg", ".png", ".jpeg", ".webp"));
  // 환경설정으로 설정한 경로를 가져오기 위함
  private final FileConfig fileConfig;
  private final FileRepository fileRepository;

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

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<Resource> download(Long id) {
    File findFile = fileRepository.findById(id)
        .orElseThrow(
            () -> new IllegalArgumentException("DB에서 해당 ID로 된 File을 찾을 수 없습니다. ID: " + id));

    // 파일 메타정보
    Long fileId = findFile.getId();
    String fileName = findFile.getFileName();
    String contentType = findFile.getContentType();
    Long fileSize = findFile.getFileSize();

      if (!fileName.contains(".")) {
          String extension = getExtensionFromContentType(contentType);
          fileName = fileName + extension;
      }

    // getPath(): ID와 contentType으로 파일이 저장된 위치를 찾습니다.
    Path targetPath = getPath(fileId, contentType);

    if (!Files.exists(targetPath)) {
      throw new IllegalArgumentException("파일 없음/읽기 불가: " + targetPath);
    }

    try {
      Resource resource = new UrlResource(targetPath.toUri());

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + fileName + "\""
          )
          .contentLength(fileSize)
          .body(resource);

    } catch (Exception e) {
      throw new RuntimeException("파일 다운로드에 실패하였습니다.");
    }
  }

    private String getExtensionFromContentType(String contentType) {
        return switch (contentType) {
            case "text/csv" -> ".csv";
            case "application/pdf" -> ".pdf";
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            default -> ".txt";
        };
    }

  /**
   * Helper,
   * 확장자에 따라 디렉토리 경로를 결정하는 공통 로직
   */
  private Path resolvePath(Long id, String extension) {
    String lowerExt = extension.toLowerCase();
    String fileName = id + lowerExt;

    if (allowedImages.contains(lowerExt)) {
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

  /**
   * Helper,
   * ID와 contentType을 받아 파일의 저장 위치를 추출합니다.
   *
   * @param id
   * @param contentType
   * @return
   */
  Path getPath(Long id, String contentType) {
    // 하기 문법은 Java 14부터 표준 문법입니다.
    return switch (contentType) {
      case "text/csv" -> fileConfig.getBackupPath().resolve(id + ".csv");
      case "text/plain" -> fileConfig.getLogPath().resolve(id + ".log");
      case "image/jpg" -> fileConfig.getProfilePath().resolve(id + ".jpg");
      case "image/jpeg" -> fileConfig.getProfilePath().resolve(id + ".jpeg");
      case "image/png" -> fileConfig.getProfilePath().resolve(id + ".png");
      case "image/webp" -> fileConfig.getProfilePath().resolve(id + ".webp");
      default -> throw new IllegalStateException("지원하지 않는 파일 형식입니다: " + contentType);
    };
  }
}
