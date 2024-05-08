FROM gradle:jdk17 AS builder

WORKDIR /app

COPY . .

RUN gradle build

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/persistence/build/libs/persistence-0.0.1-SNAPSHOT.jar /app/

CMD ["java", "-jar", "/app/persistence-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081