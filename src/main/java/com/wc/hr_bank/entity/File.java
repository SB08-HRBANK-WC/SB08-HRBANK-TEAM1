package com.wc.hr_bank.entity;

import com.wc.hr_bank.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "files")
@Getter
@Builder
@ToString(exclude = {"backup", "employee"})         // 무한 루프 방지
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 빈 생성자 만들기 방지
@AllArgsConstructor                                 // 빌더 생성시 필요함
public class File extends BaseUpdatableEntity
{
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_path", nullable = false)
    private String filePath;
}

