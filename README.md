# Web Event Pipeline

Java + Spring Boot + MySQL 기반 웹 이벤트 수집/분석 시스템

---

## 프로젝트 구조

```
event-pipeline/
├── app/                          # Spring Boot 소스코드 (여기에 복사)
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── application.properties.example
├── mysql/
│   └── init/
│       └── 01_schema.sql         # 자동 실행되는 초기 스키마
├── grafana/
│   └── provisioning/
│       ├── datasources/mysql.yml # DB 자동 연결 설정
│       └── dashboards/           # 대시보드 자동 로드
├── docker-compose.yml
└── README.md
```

---

## Step 2. 저장소 설계

### 선택한 저장소: MySQL 8.0

**선택 이유**
- 이벤트 데이터는 `event_type`, `user_id`, `created_at` 기준으로 집계 쿼리가 빈번 → RDB의 인덱스 + GROUP BY 성능이 적합
- Spring Boot + JPA와 궁합이 좋아 개발 생산성 높음
- Docker 공식 이미지로 환경 재현이 쉬움

### 스키마

```sql
CREATE TABLE events (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type  VARCHAR(50)   NOT NULL,   -- PAGE_VIEW / PURCHASE / ERROR / CLICK
    user_id     VARCHAR(100)  NOT NULL,   -- 유저 식별자
    session_id  VARCHAR(100),             -- 세션 ID
    page        VARCHAR(200),             -- 대상 페이지
    amount      DECIMAL(12,2),            -- 구매 금액 (PURCHASE)
    error_code  VARCHAR(50),              -- 에러 코드 (ERROR)
    ip_address  VARCHAR(45),              -- 클라이언트 IP
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_event_type (event_type),
    INDEX idx_user_id    (user_id),
    INDEX idx_created_at (created_at)
);
```

> JSON 통째로 저장하지 않고 필드를 분리한 이유: 집계 쿼리에서 `WHERE event_type = 'ERROR'` 처럼 특정 컬럼만 빠르게 필터링하기 위해

---

## Step 3. 분석 쿼리

### 쿼리 1 - 이벤트 타입별 발생 횟수
```sql
SELECT event_type, COUNT(*) AS count
FROM events
GROUP BY event_type
ORDER BY count DESC;
```

### 쿼리 2 - 유저별 총 이벤트 수 (Top 10)
```sql
SELECT user_id, COUNT(*) AS total_events
FROM events
GROUP BY user_id
ORDER BY total_events DESC
LIMIT 10;
```

### 쿼리 3 - 시간대별 이벤트 추이
```sql
SELECT DATE_FORMAT(created_at, '%Y-%m-%d %H:00:00') AS hour,
       event_type,
       COUNT(*) AS count
FROM events
WHERE created_at >= NOW() - INTERVAL 24 HOUR
GROUP BY hour, event_type
ORDER BY hour;
```

### 쿼리 4 - 에러 이벤트 비율
```sql
SELECT ROUND(SUM(event_type = 'ERROR') / COUNT(*) * 100, 2) AS error_rate_pct
FROM events;
```

---

## Step 4. Docker 실행 방법

### 사전 준비

1. `app/` 폴더 안에 Spring Boot 소스코드 전체를 복사
2. `app/src/main/resources/application.properties` 를 아래와 같이 수정

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/event_db}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:appuser}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:apppass}
```

### 실행

```bash
docker compose up --build
```

### 접속 URL

| 서비스 | URL | 계정 |
|--------|-----|------|
| Spring Boot API | http://localhost:8080 | - |
| Grafana 대시보드 | http://localhost:3000 | admin / admin123 |
| MySQL | localhost:3306 | appuser / apppass |

### 종료

```bash
docker compose down          # 컨테이너만 종료 (데이터 유지)
docker compose down -v       # 컨테이너 + 볼륨 삭제 (데이터 초기화)
```

---

## Step 5. 시각화 (Grafana)

`docker compose up` 실행 후 http://localhost:3000 접속하면 아래 대시보드가 자동으로 설정됩니다.

| 패널 | 차트 타입 | 분석 내용 |
|------|----------|-----------|
| 이벤트 타입별 발생 횟수 | Pie Chart | 어떤 이벤트가 가장 많이 발생했는지 |
| 시간대별 이벤트 추이 | Time Series | 24시간 동안 이벤트 흐름 |
| 유저별 총 이벤트 수 | Bar Gauge | 가장 활발한 유저 Top 10 |
| 에러 이벤트 비율 | Stat (%) | 전체 중 에러 비율, 임계값 색상 표시 |
| 총 이벤트 수 | Stat | 전체 누적 이벤트 수 |

---

## 기술 스택

| 레이어 | 기술 |
|--------|------|
| 언어/프레임워크 | Java 17 + Spring Boot |
| ORM | Spring Data JPA |
| DB | MySQL 8.0 |
| 시각화 | Grafana 10.4 |
| 컨테이너 | Docker + Docker Compose |
