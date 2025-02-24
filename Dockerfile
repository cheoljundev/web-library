FROM openjdk:17-slim-bullseye
COPY build/libs/web-library-1.0.0.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
