FROM openjdk:11-jre-slim

EXPOSE 8080

WORKDIR /app

COPY ./target/*.jar ./application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar"]