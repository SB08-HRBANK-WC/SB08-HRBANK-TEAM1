package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.response.file.FileDto;
import com.wc.hr_bank.entity.File;
import org.springframework.core.io.Resource;

public interface FileService
{
  /**
   * 실제 저장된 파일을 가져오기 위한 메서드,
   *
   * @param file FileEntity
   * @return Binary File
   */
  Resource getFile(File file);

  /**
   * 파일을 다운로드 하기 위한 메서드,
   *
   * @param fileId 다운로드 받으려는 File의 ID
   * @return 다운로드 됨
   */
  FileDto fileDownload(Long fileId);
}
