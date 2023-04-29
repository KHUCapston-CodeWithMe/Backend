FROM openjdk:11
ARG JAR_FILE=build/libs/concoder-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE=prod

RUN apt-get update -y && \
    apt-get install build-essential -y

ENTRYPOINT ["java","-jar","/app.jar"]