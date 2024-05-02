FROM gradle:jdk17 as builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts /app/

COPY src /app/src/

RUN gradle build

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/webapp-0.0.1-SNAPSHOT.jar /app/

CMD ["java", "-jar", "/app/webapp-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080