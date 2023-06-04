# 소비일기(SOIL)

* 나의 소비기록을 기록하고 공유하는 서비스 소비일기 개발 프로젝트 백엔드(서버) 소스코드 저장소
* 기간 : 2022.08 ~ 2023.05
* 환경 : WEB, RESTful API
* 개발 툴 : IntelliJ IDEA, MySQL Workbench 8.0.20
* 사용 언어 : Java 11, MySQL 8
* 사용 기술 : Spring Boot, JPA, Gradle, Open API(네이버 로그인 API), AWS(EC2, RDS, S3), Swagger

# Application UI
<p align="center"><img src="devlog/soil-preview-img.png" width="800"></p>

# Architecture
<p align="center"><img src="devlog/architecture.png" width="600"></p>

# ERD 다이어그램
<p align="center"><img src="devlog/soil-erd.png" width="800"></p>

# 주요 기능
* [각 기능별 비즈니스 로직](devlog/soil-business-logic.md)
* [각 기능별 Use Case](devlog/soil-use-case.md)<br><br><br>

## 사용자
1. 회원가입 / 탈퇴
2. 소셜로그인
3. 일기 작성 / 수정 / 삭제 / 조회
4. 키워드로 게시글 검색
5. 카테고리 생성 / 수정 / 삭제 / 조회

# 개발과정
1. [Mac apache httpd로 웹 서버 구축하기](devlog/2022-08-21-soil-dev-log-01.md)
2. [DB 테이블 설계와 생성(with JPA)](devlog/2022-08-24-soil-dev-log-02.md)
3. [회원 엔티티 개발(with JPA)](devlog/2022-08-25-soil-dev-log-03.md)
4. [API 서버 개발_일기 목록](devlog/2022-08-27-soil-dev-log-04.md)
5. [서버에서 CORS 차단문제 해결하기](devlog/2022-08-30-soil-dev-log-05.md)
6. [로그인/로그아웃 API 개발](devlog/2022-08-30-soil-dev-log-06.md)
7. [CORS를 해결하기 위한 여정](devlog/2022-09-04-soil-dev-log-07.md)
8. [AWS 서버에 무료로 HTTPS 적용하기](https://velog.io/@miro7923/AWS-EC2-서버-도메인-없이-https-붙이기)
