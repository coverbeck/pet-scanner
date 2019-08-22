FROM gradle:5.6-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:11-jre-slim
RUN mkdir /app

COPY --from=build /home/gradle/src/build/distributions/pet-scanner.jar /app/pet-scanner.jar

ENTRYPOINT ["java", "-jar","/app/pet-scanner.jar"]