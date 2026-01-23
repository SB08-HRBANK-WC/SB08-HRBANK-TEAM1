package com.wc.hr_bank.service;

import com.wc.hr_bank.dto.response.file.FileDto;

public interface FileService
{
  /**
   * 파일을 다운로드 하기 위한 메서드,
   *
   * @param fileId 다운로드 받으려는 File의 ID
   * @return 다운로드 됨
   */
  FileDto fileDownload(Long fileId);
}
