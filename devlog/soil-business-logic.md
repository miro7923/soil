# SOIL 기능별 비즈니스 로직

## User
* SOIL 서비스를 이용하기 위해서는 네이버 계정을 사용해 회원가입을 해야한다.
* 사용자는 일기를 작성할 수 있으며 이를 `Diary`라 칭한다.
* 사용자는 자신이 작성한 일기들을 메인페이지에서 볼 수 있다.
* 사용자는 키워드 검색을 통해 자신이 작성한 일기들 중 원하는 일기를 찾아볼 수 있다.

## Diary
* 사용자는 일기를 작성할 때 최대 1개의 사진을 첨부할 수 있다.
* 사용자는 일기를 작성할 때 1개의 대분류를 선택할 수 있으며 이를 `Category`라 칭한다.
* 사용자는 작성한 일기를 수정할 때 텍스트와 사진 모두 수정할 수 있다.

## Category
* 사용자는 카테고리의 생성, 수정, 삭제, 조회를 할 수 있다.