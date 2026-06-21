# Web Event Pipeline

Java + Spring Boot + MySQL 기반 웹 이벤트 수집/분석 시스템

---

## 1. 실행 방법

### 필요한 도구
- Docker Desktop
- Git

### 실행 명령어

```bash
# 1. 레포지토리 클론
git clone https://github.com/jy9s/liveclass.git

# 2. 프로젝트 폴더로 이동
cd liveclass

# 3. 실행
docker compose up --build
```

### 접속 URL

| 서비스 | URL | 계정 |
|--------|-----|------|
| Spring Boot API | http://localhost:8080 | - |
| Grafana 대시보드 | http://localhost:3000 | admin / admin123 |
| MySQL | localhost:3307 | appuser / apppass |

> `docker compose up --build` 실행 시 이벤트 100개가 자동으로 생성되어 저장됩니다.

---

## 2. 스키마 설명

### 테이블: `event_log`

| 컬럼 | 타입 | 설명 |
|------|------|------|
| id | BIGINT | 기본키 |
| user_id | BIGINT | 이벤트를 발생시킨 유저 |
| event_type | VARCHAR | 이벤트 종류 (PAGE_VIEW / PURCHASE / ERROR) |
| created_at | DATETIME | 이벤트 발생 시간 |
| page_url | VARCHAR | PAGE_VIEW 전용 |
| product_id | BIGINT | PURCHASE 전용 |
| amount | INT | PURCHASE 전용 |
| error_code | VARCHAR | ERROR 전용 |

### 설계 이유

JSON으로 통째로 저장하지 않고 필드를 분리한 이유는 `event_type`, `user_id` 같은 특정 컬럼으로 필터링과 집계가 쉬워지기 때문이다. 또한 이벤트 타입별로 필요한 데이터가 달라서 (PAGE_VIEW는 pageUrl, PURCHASE는 amount, ERROR는 errorCode) 타입에 맞는 컬럼만 저장하도록 설계했다.

---

## 3. 구현하면서 고민한 점

**저장소 선택 (MySQL)**
Oracle만 사용해본 경험이 있었지만, MySQL은 오픈소스로 무료이고 Spring Boot와 MySQL 조합이 실무에서 많이 쓰인다고 알고 있어서 이번 기회에 직접 사용해보고 싶었다.

**이벤트 설계**
이벤트 타입별로 필요한 컬럼이 다르기 때문에 switch문으로 타입에 맞는 필드만 저장하도록 구현했다. 이렇게 하면 불필요한 데이터 없이 타입별 필터링이 쉬워진다고 판단했다.

**Docker**
Docker는 처음 사용해봐서 docker-compose 설정과 컨테이너 간 연결에 어려움이 있었다. 로컬에 MySQL이 이미 설치되어 있어 포트 충돌이 발생했고, 포트를 3307로 변경해서 해결했다. 전반적인 설정은 AI의 도움을 받아 구성했다.

---

## 기술 스택

| 레이어 | 기술 |
|--------|------|
| 언어/프레임워크 | Java 17 + Spring Boot |
| ORM | Spring Data JPA |
| DB | MySQL 8.0 |
| 시각화 | Grafana 10.4 |
| 컨테이너 | Docker + Docker Compose |
