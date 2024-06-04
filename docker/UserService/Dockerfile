# Dockerfile for Spring Boot service 1
FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./UserService/User-Service.jar .
COPY ./UserService/application.properties .

CMD ["java", "-jar", "User-Service.jar"]
