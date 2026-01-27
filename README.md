# TEAM WOONG-CHICKEN

## TEAM ([NOTION 📋]((https://ohgiraffers.notion.site/1-2ee649136c118001a6bff62d4a602748?source=copy_link)))
| <img src="https://github.com/Junkov0.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/xxzeroeight.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/castle-bird.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/idktomorrow.png" width="100" style="border-radius:50%"/> | <img src="https://github.com/seungwon00.png" width="100" style="border-radius:50%"/> |
|:---------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------|
|                                      **최준영**                                      |                                        **김경태**                                        |                                        **박성조**                                        |                                        **선웅제**                                        | **현승원**                                                                              |
|                      [@Junkov0](https://github.com/Junkov0)                       |                    [@xxzeroeight](https://github.com/xxzeroeight)                     |                    [@castle-bird](https://github.com/castle-bird)                     |                    [@idktomorrow](https://github.com/idktomorrow)                     | [@seungwon00](https://github.com/seungwon00)                                         |
|                                 팀장, PM, 수정 이력 관리                                  |                                     형상 관리, 백업 관리                                      |                                     문서 관리, 파일 관리                                      |                                     DB 관리, 부서 관리                                      | DB 관리, 직원 관리                                                                         |

1. 프로젝트 개요 (1분)
2. 팀 소개 (이미지+이름) 간단하게 (30~1분)
3. 주요 기능 -> 하위 기술 상세 (o1) (3분)
4. 기술 스택 (30초)
5. 브랜치 전략, 컨벤션, 코드 리뷰 프로세스 (1분~1분 30초)
6. 프로젝트 구조 (이런 구조를 선택한 이유) (1분)
7. ERD (ERD넣고 이미지 사이즈를 보고, 이런 ERD를 선택한 이유) (2분)
8. 트러블 슈팅, 기술적 도전과제 (2분~3분)
   9. 작업 시 병목 현상
10. 시연 (3분)

## 프로젝트 개요
### 프로젝트 소개
**HR Bank**는 기업의 인사 정보를 체계적으로 관리하고 안전하게 백업하는 HR 관리 시스템입니다.

### 프로젝트 정보
- **기간**: 2026.01.19 ~ 2026.01.29
- **인원**: 5명
- **역할**: HR 관리 시스템의 Backend 개발

## 주요 기능
부서 관리
- 부서를 관리합니다.

직원 관리
- 직원을 관리합니다.

파일 관리
- 파일을 관리합니다.

백업 관리
- 백업을 관리합니다.

수정 이력 관리
- 수정 이력과 수정 이력 상세를 관리합니다.

## 🛠 기술 스택
#### Backend
- **Java** 17
- **Spring Boot** 3.5.9
- **Spring Framework** 6.2.15
- **Spring Data JPA** 3.5.7

#### Database
- **H2** 2.3.232 (dev)
- **PostgreSQL** 42.7.8 (prod)

#### Build Tool
- **Gradle** 8.14.3

#### Libraries
- **MapStruct** 0.2.0
- **Springdoc-OpenAPI** 5.21.0
- **Lombok** 1.18.42

#### Deployment
- **Railway.io**

## 구현 기능 상세
최준영
[움짤]
- 주요 기능보다 자세히 설명

김경태
[움짤]
- 주요 기능보다 자세히 설명

박성조
[움짤]
- 주요 기능보다 자세히 설명

선웅제
[움짤]
- 주요 기능보다 자세히 설명

현승원
[움짤]
- 주요 기능보다 자세히 설명

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

## API 문서
[API 문서]()

## 구현 사이트 (데모)
[구현 사이트]()