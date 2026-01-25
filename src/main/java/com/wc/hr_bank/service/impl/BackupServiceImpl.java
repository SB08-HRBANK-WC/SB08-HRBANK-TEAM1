package com.wc.hr_bank.service.impl;

import com.wc.hr_bank.dto.response.backup.BackupDto;
import com.wc.hr_bank.entity.Backup;
import com.wc.hr_bank.entity.Employee;
import com.wc.hr_bank.entity.File;
import com.wc.hr_bank.entity.StatusType;
import com.wc.hr_bank.mapper.BackupMapper;
import com.wc.hr_bank.repository.BackupRepository;
import com.wc.hr_bank.repository.ChangeLogRepository;
import com.wc.hr_bank.repository.EmployeeRepository;
import com.wc.hr_bank.repository.FileRepository;
import com.wc.hr_bank.service.BackupService;
import com.wc.hr_bank.storage.FileStorage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BackupServiceImpl implements BackupService
{
    private final BackupRepository backupRepository;
    private final ChangeLogRepository changeLogRepository;
    private final FileRepository fileRepository;
    private final EmployeeRepository employeeRepository;
    private final FileStorage fileStorage;

    private @Value("${hr-bank.file_directories.logs:./logs}") String root;

    private final BackupMapper backupMapper;

    /**
     * 특정 상태의 가장 최근 백업 정보를 조회,
     * 상태를 특정하지 않으면 COMPLETED
     *
     * @param status 특정 상태
     * @return 가장 최근 백업 정보
     */
    @Override
    public BackupDto getLatest(StatusType status) {
        StatusType targetStatus = (status != null) ? status : StatusType.COMPLETE;

        return backupRepository.findLatestByStatus(targetStatus)
                .map(backupMapper::toDto)
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * API 요청에 의한 데이터 백업,
     *
     * @param httpServletRequest HTTP 요청 객체
     * @return 백업 결과 DTO
     */
    @Override
    public BackupDto createBackup(HttpServletRequest httpServletRequest) {
        String worker = extractIpAddress(httpServletRequest);
        return executeBackup(worker);
    }

    /**
     * 배치에 의한 데이터 백업 (1시간 간격),
     *
     * @return 백업 결과 DTO
     */
    @Override
    @Scheduled(cron = "${backup.batch.schedule.cron:0 0 * * * *}") // 간단하고 짧아서 따로 분리하지 않음.
    public BackupDto batchBackup() {
        return executeBackup("system");
    }

    /**
     * 백업 실행 공통 로직,
     *
     * @param worker 작업자 (IP or "system")
     * @return 백업 결과 DTO
     */
    private BackupDto executeBackup(String worker) {
        Instant now = Instant.now();

        // STEP 1: 백업 여부 판단
        if (!needsBackup()) {
            // 백업이 필요없는 경우 (건너뜀)
            Backup backup = new Backup(worker, StatusType.SKIPPED, now, now);
            backupRepository.save(backup);

            return backupMapper.toDto(backup);
        }

        // STEP 2: 백업 이력 등록 (진행중)
        Backup backup = new Backup(worker, StatusType.IN_PROGRESS, now, null);
        backupRepository.save(backup);

        // STEP 3, 4: 실제 백업 실행
        File file = null;
        try {
            // STEP 3: CSV 파일 생성 및 백업 수행
            file = performBackupStream(backup, now);

            // STEP 4-1: 성공
            return backupSuccess(backup, file);
        } catch (Exception e) {
            // STEP 4-2: 실패
            return backupFailure(backup, now, file, e);
        }
    }

    /**
     * 스트리밍 방식으로 CSV 백업 수행,
     * 페이징: 구현 간단/대용량 데이터에서 비효율적
     * 스트리밍: 메모리 효율 좋음/트랜잭션 관리 복잡
     *
     * @param startedAt 백업 시작 시간
     * @param backup 백업하려는 백업 엔티티
     * @return 파일 엔티티
     */
    private File performBackupStream(Backup backup, Instant startedAt) throws IOException {
        // 임시 엔티티 (임시 정보로 먼저 저장 -> ID 획득 -> 물리적 파일 생성 -> 최종 정보로 업데이트)
        File file = fileRepository.save(new File("temp", "text/csv", 0L, null));

        try (OutputStream csv = fileStorage.save(file.getId(), ".csv");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(csv, StandardCharsets.UTF_8))) {

            writer.write('\uFEFF');
            writer.write("ID,직원번호,이름,이메일,부서,직급,입사일,상태\n");

            // 10,000명의 직원이 있다고 가정했을 때,
            // 100: 메모리 사용은 1000보다 적지만, 쿼리 횟수가 많아짐.
            // 1000: 메모리 사용은 100보다 많지만, 쿼리 횟수가 적어짐.
            int pageSize = 1000;
            int pageNo = 0;
            Page<Employee> employeePage;

            do {
                employeePage = employeeRepository.findAll(
                        PageRequest.of(pageNo++, pageSize)
                );

                for (Employee employee : employeePage.getContent()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s\n",
                            employee.getId(),
                            employee.getEmployeeNumber(),
                            employee.getName(),
                            employee.getEmail(),
                            employee.getDepartment().getName(),
                            employee.getJobTitle(),
                            employee.getJoinedAt(),
                            employee.getStatus()
                    ));
                }

            } while (employeePage.hasNext());
        }

        String fileName = startedAt.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Path filePath = Paths.get(root + file.getId() + ".csv");
        Long fileSize = Files.size(filePath);

        file.update(fileName, "text/csv", fileSize, null);
        fileRepository.save(file);

        return file;
    }

    /**
     * 백업 실패,
     * 백업이 실패하면 기존의 .csv파일을 삭제한 뒤 에러 로그 파일을 생성한다.
     *
     * @param backup 백업 인스턴스
     */
    @Transactional
    public BackupDto backupFailure(Backup backup, Instant startedAt, File csvfile, Exception e) {
        // STEP 4-2-1: 실패시 CSV 파일 삭제
        if (csvfile != null) {
            fileRepository.delete(csvfile);
        }

        try {
            // 임시 엔티티 (임시 정보로 먼저 저장 -> ID 획득 -> 물리적 파일 생성 -> 최종 정보로 업데이트)
            File logFile = new File("temp", "text/csv", 0L, null);
            fileRepository.save(logFile);

            try (OutputStream logs = fileStorage.save(logFile.getId(), ".log");
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(logs, StandardCharsets.UTF_8))) {

                writer.write("백업 실패 이유:\n");
                writer.write(e.getMessage() != null ? e.getMessage() : "알 수 없는 오류");
                writer.write("\n\n");
            }

            // STEP 4-2-2: 에러 로그를 .log 파일로 저장
            String fileName = startedAt.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            Path filePath = Paths.get(root + logFile.getId() + ".log");
            Long fileSize = Files.size(filePath);

            logFile.update(fileName, "text/plain", fileSize, null);
            File savedLogFile = fileRepository.save(logFile);

            // STEP 4-2-3: 백업 상태 업데이트
            backup.update(Instant.now(), savedLogFile, StatusType.FAILED);
            backupRepository.save(backup);

            return backupMapper.toDto(backup);

        } catch (IOException ioException) {
            throw new RuntimeException("에러 로그 파일 생성 실패", ioException);
        }
    }

    /**
     * 백업 성공,
     * 백업에 성공하면 백업 엔티티의 상태를 변경해준다.
     *
     * @param backup 백업 인스턴스
     * @param file .csv 파일
     */
    @Transactional
    public BackupDto backupSuccess(Backup backup, File file) {
        backup.update(Instant.now(), file, StatusType.COMPLETE);

        backupRepository.save(backup);
        return backupMapper.toDto(backup);
    }

    /**
     * 백업 필요 여부를 판단,
     * 가장 최근 완료된 배치 작업 시간(EndedAt) 이후(isAfter) 직원 데이터가 변경(UpdatedAt)된 경우에 데이터 백업이 필요한 것으로 간주
     *
     * @return 백업 여부
     */
    private Boolean needsBackup() {
        Instant lastEndedAt = backupRepository.findTopByOrderByEndedAtDesc();
        Instant lastUpdatedAt = changeLogRepository.findTopByOrderByUpdatedAtDesc();

        if (lastEndedAt == null) {
            return true;
        }

        if (lastUpdatedAt == null) {
            return true;
        }

        return lastUpdatedAt.isAfter(lastEndedAt);
    }

    /**
     * HTTP 요청에서 IP 추출,
     *
     * @param httpServletRequest 서블릿 요청 객체
     * @return 작업자의 IP
     */
    private String extractIpAddress(HttpServletRequest httpServletRequest) {
        String ipAddress = httpServletRequest.getHeader("X-Forwarded-For"); // Header

        if (ipAddress != null && !ipAddress.isBlank()) {
            return ipAddress.split(",")[0].trim();
        }

        return httpServletRequest.getRemoteAddr(); // 프록시의 IP를 리턴. (실제 IP를 모름)
    }
}
