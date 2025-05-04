# Build Stage
# open JDK 17 기반 이미지
FROM openjdk:17-jdk-slim

# 작업 디렉토리
WORKDIR /app

# JAR 파일, 설정 파일 컨테이너로 복사
COPY cloop-0.0.1-SNAPSHOT.jar /app/app.jar
COPY application.yml /app/config/application.yml
#COPY .env /app/

EXPOSE 8080

# 컨테이너 실행 시 명령어
ENTRYPOINT [
  "java",
  "-jar",
  "-Dspring.config.location=/app/config/application.yml",
  "/app/app.jar"
]