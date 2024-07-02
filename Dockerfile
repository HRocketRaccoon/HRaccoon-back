FROM openjdk:17

WORKDIR /home/ubuntu

# Install tzdata package
RUN apt-get update && apt-get install -y tzdata

# Set the timezone
ENV TZ=Asia/Seoul
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app.jar

ENV USE_PROFILE=prod

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${USE_PROFILE}", "/app.jar"]