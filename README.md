# TEAM WOONG-CHICKEN

## TEAM
| <img src="https://github.com/Junkov0.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/xxzeroeight.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/castle-bird.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/idktomorrow.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/seungwon00.png" width="100" style="border-radius:50%"/> |
|:---------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------|
|                                      **최준영**                                      |                                        **김경태**                                        |                                        **박성조**                                        |                                        **선웅제**                                        | **현승원**                                                                              |
|                      [@Junkov0](https://github.com/Junkov0)                       |                    [@xxzeroeight](https://github.com/xxzeroeight)                     |                    [@castle-bird](https://github.com/castle-bird)                     |                    [@idktomorrow](https://github.com/idktomorrow)                     | [@seungwon00](https://github.com/seungwon00)                                         |
|                                 팀장, PM, 수정 이력 관리                                  |                                     형상 관리, 백업 관리                                      |                                     문서 관리, 파일 관리                                      |                                     DB 관리, 부서 관리                                      | DB 관리, 직원 관리                                                                         |

[TEAM NOTION]((https://ohgiraffers.notion.site/1-2ee649136c118001a6bff62d4a602748?source=copy_link))
<br/>

## 프로젝트 개요
### 프로젝트 명
HR BANK

### 프로젝트 소개
**HR Bank**는 기업의 인사 정보를 체계적으로 관리하고 안전하게 백업하는 HR 관리 시스템입니다.

### 프로젝트 정보
- **기간**: 2026.01.19 ~ 2026.01.29
- **인원**: 5명
- **역할**: HR 관리 시스템의 Backend 개발

<br/>

## 주요 기능
수정 이력 관리
- 수정 이력 상세 조회를 포함한 직원 정보 변경 이력 통합 관리

직원 관리
- 직원 CRUD 및 목록 조회(정렬/페이지네이션) 기능 제공

파일 관리
- 파일 I/O 스트림 전달 및 다운로드 기능

백업 관리
- 직원 데이터를 csv 파일로 백업하고, 백업 이력을 조회/관리

부서 관리
- 부서 등록, 수정, 삭제 기능과 전체 조회 및 조건별 정렬 기능 제공

<br/>

## 기술 스택
#### Backend
- **Java** 17
- **Spring Boot** 3.5.9
- **Spring Framework** 6.2.15
- **Spring Data JPA** 3.5.7

#### Database
- **H2** 2.3.232 (dev)
- **PostgreSQL** 18.1 (prod)

#### Build Tool
- **Gradle** 8.14.3

#### Libraries
- **MapStruct** 0.2.0
- **Springdoc-OpenAPI** 5.21.0
- **Lombok** 1.18.42

#### Deployment
- **Railway.io**

<br/>

## 구현 기능 상세
### **최준영 (수정 이력 관리)**
![수정 이력 관리](https://cdn.discordapp.com/attachments/1432587976189153357/1465959317923434730/62234340e60f7e05.gif?ex=697b0081&is=6979af01&hm=9e22cc7adc1c903f12ed6f0314e7003a1c257763c60f0b35a4a90cd0ae551744&)

변경 단위 중심 이력 기록
- 엔티티 상태 스냅샷 비교 기반 Diff 생성 및 ChangeLog 생성 
- 수정 전·후 객체를 비교하여 **변경된 속성만** `before / after` 형태로 저장
- 불필요한 데이터 중복 없이, 각 변경 행위의 의미와 영향을 명확히 식별 가능

조건 조합형 변경 이력 탐색
- 다중 검색 조건 기반 동적 조회 쿼리
- 사번, 이 유형, 메모, IP 주소, 시간 범위를 **자유롭게 조합**하여 변경 이력을 탐색 
- 특정 직원, 특정 행위, 특정 시점의 변경 흐름을 빠르게 역추적 가능

이력 메타데이터 중심 설계 
- ChangeLog(헤더) / ChangeLogDiff(상세) 분리 모델링
- 목록 조회 시에는 변경 요약 정보만 제공하고, 상세 조회에서만 실제 변경 내용을 로딩
- 조회 성능 최적화 및 네트워크 비용 최소화
- 로그 조회 빈도가 높은 환경에서도 네트워크 및 메모리 사용 최소화

<br/>

### **김경태 (백업 관리)**
![백업 관리](https://cdn.discordapp.com/attachments/1432587976189153357/1465961738070659246/2821d4af97e7f6e9.gif?ex=697b02c2&is=6979b142&hm=919bcfcc1ebc1116722a216d7c7df6ce8569f4f75b625bbf463d0def7e206b1e&)

데이터 백업
- 직원 정보 변경을 자동으로 감지하고, 변경이 발생한 경우에만 CSV 파일로 백업을 생성
- 많은 양의 데이터를 처리할 때 발생할 수 있는 OOM 현상을 방지하기 위해, 데이터를 스트리밍 방식으로 처리
- 각 백업 작업의 실패 시 로그를 남겨 추적 가능

자동 배치 백업
- Spring의 스케줄링 기능을 활용하여 1시간마다 자동으로 백업 작업이 수행되도록 구현
- 백업 실행 주기는 설정 파일을 통해 운영 환경에 맞게 조정 가능

백업 이력 조회
- 작업자 IP 주소(부분 검색), 백업 시작 시간(기간 지정), 백업 상태(정확한 값) 등 다양한 검색 조건을 지원
- 시간 시간 또는 완료 시간을 기준으로 오름차순/내림차순 정렬이 가능
- 대량의 백업 이력을 효율적으로 탐색할 수 있도록 커서 페이지네이션 방식을 적용

<br/>

### **박성조 (파일 관리)**
![대시보드 관리](https://cdn.discordapp.com/attachments/1432587976189153357/1465962028295258344/cbf5212419b95b48.gif?ex=697b0307&is=6979b187&hm=885a627ab42da09ce34912a7dcee16f1a1956f647ebfb6bc8a900929fecd6b84&)

파일 I/O 연결 계층 개발
- 다양한 파일 유형(프로필 이미지 .jpg/png, 백업 .csv, 로그 .log)을 확장자별 분리 저장하는 I/O 연결 계층 개발
- Stream Interface로 호출자 생산성 극대화 (OutputStream save(), InputStream get())
- RESTful 다운로드 API 완성 (GET /files/{id}/download)
- 확장성: 화이트리스트 + Switch Expression
- 안전성: 파일 존재 검증 + 트랜잭션 연계
- 유지보수: @FileConfig 일괄 관리

아키텍처 설계
- Stream 기반 추상화: 파일 경로 직접 전달 → 타입세이프 인터페이스
- OutputStream save(Long id, String extension): 자유로운 파일 내용 작성
- InputStream get(Long id, String extension): 즉시 파일 읽기 제공

<br/>

### **선웅제 (부서 관리)**
![부서 관리](https://cdn.discordapp.com/attachments/1432587976189153357/1465961891162493061/a72f12520f64c236.gif?ex=697b02e7&is=6979b167&hm=2f702e0d820cdb16271fc76105d1c82f5f7c98d7282db5b99a92fe38e0ac835e&)

다중 조건 동적 검색
- 부서명과 설명을 아우르는 통합 검색 지원
- 키워드 유무에 따라 쿼리 조건절이 유동적으로 변화하는 동적 JPQL 구현

검색 최적화
- SQL LIKE 패턴을 적용한 부분 일치 검색 설계 
- 일부 키워드만으로도 정확한 결과를 도출하여 사용자 검색 편의성 제공

No-Offset 페이징 아키텍처
- 마지막 식별자 기반의 커서 페이징을 도입하여 기존 OFFSET 방식의 성능 저하 해결
- 데이터 양에 관계없이 일정한 응답 속도를 유지하는 대용량 조회 최적화 수행

명시적 형변환(CAST)을 통한 쿼리 안정성
- PostgreSQL의 엄격한 타입 체크에 대응하기 위해 파라미터 바인딩 시 CAST 함수 적용
- Unknown Type 에러 등 런타임 예외를 차단하고 DB 엔진의 실행 계획 안정성 확보

객체 지향적 엔티티 연관 관계 설계
- Department와 Employee 간의 논리적 연관 관계를 물리 DB 외래 키(FK) 구조와 동기화
- 데이터 정합성 보장 및 향후 비즈니스 확장이 용이한 유연한 데이터 모델 구축

<br/>

### **현승원 (직원 관리)**
![직원 관리](https://cdn.discordapp.com/attachments/1432587976189153357/1465961854873505802/a19a513e84b174a7.gif?ex=697b02de&is=6979b15e&hm=977483c07177f1ced4c95179172d6209af0d888ddc1915831b44409f617092d8&)

직원 관리 시스템 상세 기능
- 고성능 목록 조회 (Cursor Pagination)
    - 커서 기반 페이징 및 다중 조건 동적 쿼리
    - 대용량 데이터에서도 성능 저하 없는 안정적인 조회 로직 구현
    - Offset 방식의 한계를 극복하여 일정한 시스템 응답 속도 유지

투명한 변경 이력 관리 (Audit Trail)
- 엔티티 스냅샷 비교 및 ChangeLog 자동 기록
    - 수정 전/후 데이터를 객체 단위로 비교하여 변경 사항 전수 추적
    - 누가, 언제, 어떤 데이터를 고쳤는지 기록하여 인사 데이터 무결성 보장

안정적인 사원 정보 관리 (Employee CRUD)
- MultipartFile 처리 및 비즈니스 예외 설계
    - 프로필 이미지 선택 사항화(Optional)로 등록/수정 시 안정성 확보
    - 이메일 중복 검증 등 철저한 예외 처리로 데이터 오류 원천 차단

<br/>

## 프로젝트 구조
```markdown
.
└── src/
    └── main/
        ├── java/com/wc/hr_bank/
        │   ├── controller/
        │   │   └── api
        │   ├── dto/
        │   │   ├── request/
        │   │   │   ├── bakcup
        │   │   │   ├── department
        │   │   │   ├── employee
        │   │   │   └── file
        │   │   └── response/
        │   │       ├── backup
        │   │       ├── changelog
        │   │       ├── department
        │   │       ├── employee
        │   │       ├── file
        │   │       └── ErrorResponse.java
        │   ├── entity/
        │   │   └── base
        │   ├── mapper
        │   ├── repository
        │   ├── service/
        │   │   └── impl
        │   ├── storage/
        │   │   └── impl
        │   ├── exception
        │   ├── global/
        │   │   └── config
        │   └── HrBankApplication.java
        └── resources/
            ├── static
            └── templates/
                ├── application.yaml
                └── schema.sql
```

<br/>

## API 문서
[API 문서](https://sb08-hrbank-team1-production.up.railway.app/swagger-ui/index.html)

## 구현 사이트 (데모)
[구현 사이트](https://sb08-hrbank-team1-production.up.railway.app)