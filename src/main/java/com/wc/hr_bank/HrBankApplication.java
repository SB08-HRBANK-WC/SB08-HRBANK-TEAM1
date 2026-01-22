package com.wc.hr_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HrBankApplication
{
    public static void main(String[] args) {
        SpringApplication.run(HrBankApplication.class, args);
    }
}
