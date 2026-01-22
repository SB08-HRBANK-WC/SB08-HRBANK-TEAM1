package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.FileApi;
import com.wc.hr_bank.dto.response.file.FileDto;
import com.wc.hr_bank.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController implements FileApi
{
  private final FileService fileService;

  @Override
  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable Long id) {
    FileDto fileDto = fileService.fileDownload(id);

    /**
     * 파일명이 한글이 있을 경우,
     * 깨질 수도 있기에 UTF_8로 인코딩 해야함
     * 다운로드는 attachment 타입으로 해줘야함
     */
    ContentDisposition contentDisposition = ContentDisposition.attachment()
        .filename(fileDto.fileName(), StandardCharsets.UTF_8)
        .build();

    return ResponseEntity.ok()
        .contentLength(fileDto.fileSize())
        .contentType(MediaType.parseMediaType(fileDto.contentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
        .body(fileDto.resource());
  }
}
