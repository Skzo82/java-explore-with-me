FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /work
COPY ../../pom.xml ./pom.xml
COPY ../ewm-stats-dto ../ewm-stats-dto
COPY ../ewm-stats-server ../ewm-stats-server
RUN mvn -q -DskipTests -pl statistics/ewm-stats-server -am clean package spring-boot:repackage

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /work/statistics/ewm-stats-server/target/*-SNAPSHOT.jar /app/app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/app/app.jar"]