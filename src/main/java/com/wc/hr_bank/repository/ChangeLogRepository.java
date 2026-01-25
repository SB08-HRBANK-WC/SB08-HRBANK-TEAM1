// 2. ChangeLogRepository.java
package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {
}