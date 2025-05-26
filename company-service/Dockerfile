FROM gradle:8.7.0-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

FROM eclipse-temurin:21-jre AS base
WORKDIR /app
COPY --from=builder /app/build/libs/companyservice-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
EXPOSE 8761
