# Dockerfile for Spring Boot service 1
FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./spring_service1/service1.jar .
COPY ./spring_service1/application.properties .

CMD ["java", "-jar", "service.jar"]
