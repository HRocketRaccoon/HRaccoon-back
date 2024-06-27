FROM openjdk:17

WORKDIR /home/ubuntu

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

ENV USE_PROFILE=prod

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${USE_PROFILE}", "/app.jar"]