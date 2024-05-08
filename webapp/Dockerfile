FROM gradle:jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle build

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/webapp/build/libs/webapp-0.0.1-SNAPSHOT.jar /app/

CMD ["java", "-jar", "/app/webapp-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080