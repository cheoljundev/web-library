## 온라인 도서관

김영한 스프링 로드맵을 진행하면서, 동시에 개발하는 복습 겸용 사이드프로젝트입니다.

[부천시립도서관](https://www.bcl.go.kr/)과 같은 온라인에서 도서를 대출할 수 있는 사이트입니다.
점점 도메인 및 기능을 늘려갈 예정입니다.

## 상세 문서

프로젝트에 대해서 상세하게 확인하고 싶으시면, 아래 문서를 참고해주세요!

- [온라인 도서관 프로젝트](https://devcj.kr/web-library/)


## 점진적 고도화

김영한 스프링 로드맵을 보면서, 점진적으로 프로젝트를 고도화시킬 계획입니다. 스프링 MVC1편의 프론트 컨트롤러 v5를 시작으로 개발합니다.

### 고도화 단계

- [x] 스프링 mvc1편 servlet + jsp mvc 프론트 컨트롤러 v5
- [x] 스프링 mvc1편 springmvc v3 (실용적인 컨트롤러)
- [x] Jsp -> thymeleaf로 리팩토링
- [x] thymeleaf 레이아웃 적용하기
  - [x] 헤더 푸터 적용
  - [x] 로그인과 홈페이지 분리(홈에서는 대출/반납만)
- [x] 유저 권한 변경 시 셀렉트 박스에 권한 가지고 오기
- [x] 메시지 국제화 적용하기
- [x] Validate 적용
  - [x] 로그인
  - [x] 회원가입
  - [x] 도서관리
  - [x] 도서 대출/반납
  - [x] BeanValidation 적용
- [x] 관리자 페이지 세분화 (도서관리, 회원관리 나눔)
- [x] Optional 도입
- [x] 서블릿 필터 도입
  - [x] 로그인 기능까지
- [x] 스프링 인터셉터 적용
- [x] 아규먼트 리졸버 적용
- [x] 스프링부트 오류 페이지 적용
- [x] 파일 업로드 구현
- [x] DB
  - [x] jdbc
    - [x] jdbc 템플릿
    - [x] NamedParameterJdbcTemplate
    - [x] SimpleJdbcInsert
  - [x] 트랜잭션 적용
  - [x] 페이지네이션 구현
  - [x] Mybatis 적용
  - [x] 도서 검색 기능 구현