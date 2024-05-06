FROM gradle:jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle :persistence:bootJar

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/persistence-0.0.1-SNAPSHOT.jar /app/persistence.jar

CMD ["java", "-jar", "/app/persistence.jar"]

EXPOSE 8080