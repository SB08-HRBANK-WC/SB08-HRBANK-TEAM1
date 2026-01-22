package com.wc.hr_bank.dto.response.file;

import org.springframework.core.io.Resource;

public record FileDto(
        String fileName,        // 다운로드될 때의 파일 이름
        String contentType,     // 파일 종류 (text/csv)
        long fileSize,          // 파일 크기
        Resource resource       // 실제 파일 데이터 (스트림 형태)
)
{
}
