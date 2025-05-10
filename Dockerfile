FROM openjdk:17-jdk-slim

WORKDIR /app

# JAR 복사
#COPY build/libs/app.jar /app/app.jar
COPY build/libs/*.jar /app/app.jar

# application.yml 복사 (옵션)
COPY src/main/resources/application.yml /app/config/application.yml

COPY .env ./.env

# 포트 정보 문서화 (선택)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.config.location=file:/app/config/application.yml", "/app/app.jar"]