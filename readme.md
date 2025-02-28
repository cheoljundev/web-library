# 온라인 도서관 백엔드

## 소개

**온라인 도서관** 프로젝트는 REST API를 기반으로 백엔드와 프론트엔드를 분리하여 개발하였습니다. 백엔드는 Spring Boot를 사용하고, 프론트엔드는 Next.js로 개별 개발되어 독립적으로 동작합니다.

**온라인 도서관** 프로젝트의 백엔드 레포지토리입니다. 이 프로젝트는 시립 도서관 사이트에서 영감을 받아 개발된 개인 프로젝트로, 도서 대출 및 반납 기능, 관리자용 도서 및 회원 관리 기능을 제공합니다. 사용자는 사이트에서 도서를 대출한 후, 도서관에서 관리자의 확인을 거쳐 픽업할 수 있습니다.

📄 **[프로젝트 상세 문서 보기](https://devcj.kr/web-library/)**

- **프로젝트 기간**: 2025년 1월 8일 ~ 2025년 2월 28일
- **개발 인원**: 개인 프로젝트

## 주요 기능

🔗 **[주요 기능 화면 확인](https://devcj.kr/web-library#핵심-기능)**

본 프로젝트는 REST API를 활용하여 프론트엔드(Next.js)와 원활한 통신을 지원합니다. 이를 통해 사용자는 직관적인 UI에서 백엔드 기능을 손쉽게 이용할 수 있습니다.

- **로그인 및 회원가입**: Spring Security Crypto를 이용한 사용자 인증 및 계정 생성 기능을 제공합니다.
- **도서 검색**: 다양한 필터와 검색 옵션을 통해 원하는 도서를 쉽게 찾을 수 있습니다.
- **도서 대출 및 반납**: 온라인으로 도서를 대출하고, 반납할 수 있습니다.
- **도서 관리**: 관리자는 도서 정보를 추가, 수정, 삭제할 수 있습니다.
- **회원 관리**: 관리자는 회원 정보를 조회하고, 권한을 수정하거나 삭제할 수 있습니다.
- **마이페이지**: 사용자는 자신의 정보를 확인하고, 대출한 도서를 조회 및 반납할 수 있습니다.

## 기술 스택

- **프레임워크**: Spring Boot
- **보안**: Spring Security Crypto
- **데이터베이스**: MySQL
- **빌드 도구**: Gradle
- **배포**: AWS EC2, Docker
- **CI/CD**: GitHub Actions

## 설치 및 실행

1. **레포지토리 클론**:

   ```bash
   git clone https://github.com/cheoljundev/web-library.git
   cd web-library
   ```

2. **환경 변수 설정**: 프로젝트 루트 디렉터리에 `.env` 파일을 작성하여 데이터베이스 및 보안 설정 등의 환경 변수를 설정합니다.

3. **빌드 및 실행**:

   ```bash
   ./gradlew bootJar
   java -jar build/libs/web-library-1.0.0.jar
   ```

4. **Docker를 이용한 실행**:

   프로젝트는 Docker Compose를 사용하여 실행됩니다. `docker-compose.yml` 파일을 활용하여 손쉽게 컨테이너를 관리할 수 있습니다.

   ```bash
   docker build -t web-library .
   docker compose up -d
   ```

## API 문서

API 명세서는 Swagger를 통해 자동 생성되며, 애플리케이션 실행 후 아래 주소에서 확인할 수 있습니다.

- [Swagger UI](http://localhost:8080/swagger-ui.html)

## 참고 목록

- [프론트엔드 레포지토리](https://github.com/cheoljundev/web-library-front)

---

**온라인 도서관** 프로젝트에 관심을 가져주셔서 감사합니다! 🚀

