package com.wc.hr_bank.storage;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileStorage
{

  /**
   * 파일을 먼저 생성,저장한 후, OutputStream을 연결시킵니다.
   * 
   * @param id 
   * @param extension 확장자
   * @return
   */
  OutputStream save(Long id, String extension);

  /**
   * 파일을 가져갈 수 있도록 InputStream을 연결시킵니다.
   * 
   * @param id
   * @param extension 확장자
   * @return
   */
  InputStream get(Long id, String extension);
}
