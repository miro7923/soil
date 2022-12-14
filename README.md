# 소비일기(SOIL)
* 나의 소비기록을 기록하고 공유하는 안드로이드 앱 소비일기 개발 프로젝트 백엔드(서버) 소스코드 저장소
* 기간 : 2022.08.21 ~ 진행 중
* 환경 : 안드로이드 웹뷰, RESTful API
* 개발 툴 : IntelliJ IDEA, MySQL Workbench 8.0.20
* 사용 언어 : Java 11, MySQL 8
* 사용 기술 : Spring Boot, JPA, Gradle, Open API(네이버 로그인 API), AWS(EC2, RDS, S3), Swagger

# 프로젝트 상세 내용
## 주제
```
* 그날의 소비내역에 대한 감정을 기록하고 공유할 수 있는 안드로이드 앱
```

## 목적
```
* 어떤 상품을 구매할 때엔 구매하는 이유와 구매했을 때의 감정, 그에 얽힌 추억들이 생기게 된다.
* 가끔은 어떤 상품을 충동적으로 구매한 후 시간이 많이 지난 뒤에 그 상품을 다시 발견하게 되면 이걸 왜 샀지? 싶은 상품들도 많이 생기곤 한다.
* 그래서 이러한 것들을 사진과 함께 기록해 두고 시간이 지난 후에 되돌아 보며 이 상품을 구매하던 당시의 감정을 추억할 수 있고, 
  더 나아가 건전한 소비습관을 만드는 것에 도움을 줄 수 있는 서비스를 제공하는 것을 목표로 기획하였다.
```

# 구현목표
## 서버
```
* 프론트엔드와 통신을 위한 API 서버 개발
    - 회원가입, 로그인 처리(개발완료)
    - 소셜로그인 기능(개발완료)
    - 일기 CRUD(개발완료)
    - 커뮤니티 게시판 기능(개발보류)
    - 키워드로 게시글 검색 기능(개발완료)
    - AWS S3로 파일 업로드 기능(개발완료)
    - AWS EC2로 서버 배포(개발완료)
* Spring Security와 JWT Token을 적용한 사용자 인증/인가 시스템 개발(개발완료)
* 앱 푸시
* CI/CD
```

# ERD 다이어그램
<p align="center"><img src="https://miro7923.github.io/assets/images/soil_log_02_3.png" width="600"></p>
