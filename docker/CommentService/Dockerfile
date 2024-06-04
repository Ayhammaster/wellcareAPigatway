# Dockerfile for Spring Boot service 1
FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./CommentService/Comment-Service.jar .
COPY ./CommentService/application.properties .

CMD ["java", "-jar", "Comment-Service.jar"]
