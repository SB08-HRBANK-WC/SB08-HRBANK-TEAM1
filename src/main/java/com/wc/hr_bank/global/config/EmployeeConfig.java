package com.wc.hr_bank.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 프론트엔드와 백엔드 간의 원활한 통신을 위한 설정 클래스입니다.
 */
@Configuration
public class EmployeeConfig implements WebMvcConfigurer

{

  /**
   * CORS(Cross-Origin Resource Sharing) 설정을 정의합니다.
   * 프론트엔드 서버(브라우저)에서 백엔드 API를 호출할 수 있도록 허용합니다.
   */
  @Override
  public void addCorsMappings(CorsRegistry registry)

  {
    registry.addMapping("/**") // 모든 API 경로에 대해 적용
        .allowedOrigins("*") // 모든 출처 허용 (개발 및 로컬 테스트용)
        .allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT", "OPTIONS") // 허용할 HTTP 메서드
        .allowedHeaders("*") // 모든 헤더 허용
        .maxAge(3600); // 프리플라이트(Preflight) 요청 캐싱 시간 (초)
  }

}