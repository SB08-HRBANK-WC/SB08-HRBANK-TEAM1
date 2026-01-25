package com.wc.hr_bank.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@Slf4j
public class FileConfig
{
    /**
     * File들의 저장 경로를 설정 및 폴더 초기화를 위한 @Component
     * 초기화 작업이라 Service에서 분리함
     */
    private final Path PROFILE_PATH;    // 프로필 저장 경로
    private final Path BACKUP_PATH;     // 백업 저장 경로
    private final Path LOG_PATH;        // 로그 저장 경로

    public FileConfig(
            @Value("${hr-bank.file_directories.profiles}") String profileDir,
            @Value("${hr-bank.file_directories.backups}") String backupDir,
            @Value("${hr-bank.file_directories.logs}") String logDir
    ) {
        // 현재 프로젝트 폴더 기준으로 하기 위함
        String rootPath = System.getProperty("user.dir");

        // 프로젝트 기준 + 폴더 경로
        this.PROFILE_PATH = Path.of(rootPath, profileDir);
        this.BACKUP_PATH = Path.of(rootPath, backupDir);
        this.LOG_PATH = Path.of(rootPath, logDir);

        // 초기화 → 폴더 생성
        initDirectories();
    }

    private void initDirectories() {
        try {
            // Files.createDirectories(Path): 경로가 없으면 생성, 있으면 무시
            Files.createDirectories(PROFILE_PATH);
            Files.createDirectories(BACKUP_PATH);
            Files.createDirectories(LOG_PATH);

            log.info("폴더 생성 초기화 완료");
        } catch (IOException e) {
            log.error("폴더 생성 중 예외 발생: {}", e.getMessage(), e);
            throw new RuntimeException("[FileStorage] 폴더를 생성할 수 없습니다.", e);
        }
    }

    public Path getProfilePath() {
        return PROFILE_PATH;
    }

    public Path getBackupPath() {
        return BACKUP_PATH;
    }

    public Path getLogPath() {
        return LOG_PATH;
    }
}
