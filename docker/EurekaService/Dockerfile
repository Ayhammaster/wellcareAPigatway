# Dockerfile for Spring Boot service 1
FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./EurekaService/Eurka-server.jar .
COPY ./EurekaService/application.properties .

CMD ["java", "-jar", "Eurka-server.jar"]
