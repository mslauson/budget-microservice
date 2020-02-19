FROM gradle:jdk11 as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -no-daemon

FROM openjdk:11-jdk

EXPOSE 8080

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

