package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 빈 생성자 만들기 방지
@AllArgsConstructor                                 // 빌더 생성시 필요함
public class File extends BaseUpdatableEntity
{
  @Column(name = "file_name", nullable = false, length = 255)
  private String fileName;

  @Column(name = "content_type", nullable = false, length = 255)
  private String contentType;

  @Column(name = "file_size", nullable = false)
  private Long fileSize;

  @Column(name = "file_path", nullable = false, length = 255)
  private String filePath;

  public void update(
      String fileName,
      String contentType,
      Long fileSize,
      String filePath
  ) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.fileSize = fileSize;
    this.filePath = filePath;
  }

  @Override
  public String toString() {
    return "File{" +
        "fileName='" + fileName + '\'' +
        ", contentType='" + contentType + '\'' +
        ", fileSize=" + fileSize +
        ", filePath='" + filePath + '\'' +
        '}';
  }
}

