# Build stage
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/notification-0.0.1-SNAPSHOT.jar app.jar

# Add wait-for-it script to handle database startup order
ENV SERVER_PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
