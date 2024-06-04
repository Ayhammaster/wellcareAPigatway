# Dockerfile for Spring Boot service 1
FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./PostService/Post-Service.jar .
COPY ./PostService/application.properties .

CMD ["java", "-jar", "Post-Service.jar"]
