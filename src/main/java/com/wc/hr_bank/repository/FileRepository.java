package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
  // 이 상속이 있어야 findById 메서드를 사용할 수 있습니다.
}