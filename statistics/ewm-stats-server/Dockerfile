# build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /work

COPY ./pom.xml ./pom.xml
COPY ./statistics ./statistics

RUN mvn -q -DskipTests -pl statistics/ewm-stats-server -am clean package spring-boot:repackage

# runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /work/statistics/ewm-stats-server/target/ewm-stats-server-*.jar /app/app.jar

EXPOSE 9090
ENTRYPOINT ["java","-jar","/app/app.jar"]