# 온라인 도서관 백엔드

## 소개

**온라인 도서관** 프로젝트는 REST API를 기반으로 백엔드와 프론트엔드를 분리하여 개발하였습니다. 백엔드는 Spring Boot를 사용하고, 프론트엔드는 Next.js로 개별 개발되어 독립적으로 동작합니다.

🌍 **[배포된 실제 페이지](https://web-library-front.vercel.app/)**

**온라인 도서관** 프로젝트는 REST API를 기반으로 백엔드와 프론트엔드를 분리하여 개발하였습니다. 백엔드는 Spring Boot를 사용하고, 프론트엔드는 Next.js로 개별 개발되어 독립적으로 동작합니다.

**온라인 도서관** 프로젝트의 백엔드 레포지토리입니다. 이 프로젝트는 시립 도서관 사이트에서 영감을 받아 개발된 개인 프로젝트로, 도서 대출 및 반납 기능, 관리자용 도서 및 회원 관리 기능을 제공합니다. 사용자는 사이트에서 도서를 대출한 후, 도서관에서 관리자의 확인을 거쳐 픽업할 수 있습니다.

- **프로젝트 기간**: 2025년 1월 8일 ~ 2025년 2월 28일
- **개발 인원**: 개인 프로젝트

## 주요 기능

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

## 시스템 설계

![image](https://github.com/user-attachments/assets/f1ad0d86-5bb8-42cd-a7a4-dba381fa1e59)

### RESTful API

해당 프로젝트는 포트폴리오를 위한 프로젝트이기도 하지만, 개인의 학습에 중점을 둔 프로젝트이므로, 처음부터 RESTful API로 설계하지는 않았습니다. Jsp -> Thymeleaf -> RESTful API 순으로 점진적으로 작업하였습니다. 최종적으로는 Web Server와 Web Application Server를 분리하는 RESTful API설계를 채택하였습니다.

Frontend는 NextJs를 Vercel을 통해 배포하였고, Backend는 SpringBoot를 AWS EC2에 배포하였습니다.

Frontend로 NextJs를 선택한 이유는, 백엔드 개발자 입장에서 편의성이 있었습니다. 바닐라 ReactJs를 사용할 경우 라우팅 라이브러리, 레이아웃 설정 등 복잡함이 있으나, NextJs는 파일 기반 네스팅되는 레이아웃과 라우팅 기능을 가지고 있어 보다 편리하게 프론트엔드를 작업할 수 있었습니다. 또한 Vercel을 통한 배포 편의성도 가져갈 수 있었습니다. Github에 push와 동시에 Vercel에서 배포가 가능하다는 장점이 있습니다.

### Github Action을 통한 CI/CD

![image](https://github.com/user-attachments/assets/3ecbd582-c2b0-4656-9c0c-1a80d7468c11)

ws ec2 서버 과부하를 줄이기 위한 방법으로, Docker Hub를 사용하는 방법을 채택했습니다.

job은 빌드와 docker hub로 push하는 build, aws ssh에 접속하고 docker hub에서 이미지를 pull하고 실행하는 depoly 두 가지로 구성하였습니다.

**git action의 Build job에서는**

1. Gradle을 통한 빌드
2. Docker Hub 로그인
3. Docker 이미지 빌드
4. Docker Hub로 이미지 Push

**github action의 deploy job에서는**

1. ssh를 통해 aws ec2 인스턴스에 연결
2. Docker Hub에서 Docker 이미지 Pull
3. Dokcer Compose 명령어로 도커 이미지 실행
4. SpringBoot Image (Docker Hub를 통해 Pull해온 이미지)
5. MySql Image (docker-compose.yml에 작성됨)


## ERD

![image](https://github.com/user-attachments/assets/0110a8d6-af2e-4d8b-8c2d-3568e9dc3069)


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

