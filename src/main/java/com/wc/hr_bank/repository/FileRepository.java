package com.wc.hr_bank.repository;

import com.wc.hr_bank.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long>
{
}
