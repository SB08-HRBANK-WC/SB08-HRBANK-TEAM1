package com.wc.hr_bank.controller;

import com.wc.hr_bank.controller.api.FileApi;
import com.wc.hr_bank.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileController implements FileApi {

  private final FileService fileService;

  @Override
  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable Long id)
  {
    return null;
  }
}
