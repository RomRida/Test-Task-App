FROM openjdk:17-jdk-alpine3.14

RUN apk --no-cache add curl

RUN mkdir /app
WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]