FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# pom.xml 먼저 복사 → 의존성 캐시 활용
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 소스 복사 후 빌드
COPY src ./src
RUN mvn package -DskipTests -B

# -------- 실행 이미지 (가볍게) --------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
