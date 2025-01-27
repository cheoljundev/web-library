## 온라인 도서관

김영한 스프링 로드맵을 진행하면서, 동시에 개발하는 복습 겸용 사이드프로젝트입니다.

[부천시립도서관](https://www.bcl.go.kr/)과 같은 온라인에서 도서를 대출할 수 있는 사이트입니다.
점점 도메인 및 기능을 늘려갈 예정입니다.

## 문서

작업을 하면서 블로그에 작성한 아티클, 이슈 문서입니다.

- [article](https://devcj.kr/category/projects/web-library/web-library-articles/)
- [issue](https://devcj.kr/category/projects/web-library/web-library-issues/)


## 도메인별 주요 기능 정리

### User 도메인

- 주요 책임
  - 개별 사용자의 행위를 처리하고, 사용자 정보를 관리.

- 주요 기능
  - 회원 가입: 사용자의 정보를 저장.
  - 로그인: 사용자 인증 및 세션 관리.
  - 도서 대출: 사용자가 도서를 빌리고, 대출 기록 관리.
  - 도서 반납: 대출 기록을 업데이트하고, 해당 도서를 반납 처리.

### Admin 도메인

- 주요 책임 
  - 시스템 관리 및 관리자 전용 작업 처리.

- 주요 기능
  - 회원 관리
    - 회원 삭제, 권한 변경 (예: Admin, User).
  - 도서 관리
    - 등록: 새로운 도서를 추가.
    - 삭제: 시스템에서 특정 도서를 제거.
    - 수정: 도서 정보를 업데이트.
   
### Book 도메인

- 주요 책임
  - 도서 정보 관리 및 관련 작업 처리.

- 주요 기능
  - 도서 등록: 새 도서를 시스템에 저장.
  - 도서 삭제: 특정 도서를 시스템에서 제거.
  - 도서 수정: 도서의 세부 정보를 업데이트.
  - 도서 대출 & 반납: 대출 상태를 갱신하여 반납 처리.
  - 도서 조회: 도서 정보 검색 및 필터링 제공.


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
