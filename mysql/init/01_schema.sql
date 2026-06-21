-- =========================================
-- 이벤트 로그 테이블
-- =========================================
CREATE DATABASE IF NOT EXISTS event_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE event_db;

CREATE TABLE IF NOT EXISTS events (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type  VARCHAR(50)  NOT NULL COMMENT '이벤트 타입 (PAGE_VIEW, PURCHASE, ERROR 등)',
    user_id     VARCHAR(100) NOT NULL COMMENT '유저 식별자',
    session_id  VARCHAR(100)          COMMENT '세션 ID',
    page        VARCHAR(200)          COMMENT '대상 페이지/URL',
    amount      DECIMAL(12,2)         COMMENT '구매 금액 (PURCHASE 타입일 때)',
    error_code  VARCHAR(50)           COMMENT '에러 코드 (ERROR 타입일 때)',
    ip_address  VARCHAR(45)           COMMENT '클라이언트 IP',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '이벤트 발생시각',
    INDEX idx_event_type (event_type),
    INDEX idx_user_id    (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB COMMENT='웹 서비스 이벤트 로그';
