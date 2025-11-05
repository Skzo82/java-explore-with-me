# build stage
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /work

COPY ./pom.xml ./pom.xml
COPY ./statistics ./statistics

RUN mvn -q -DskipTests \
    -pl statistics/ewm-main-service -am \
    clean package spring-boot:repackage

# runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /work/statistics/ewm-main-service/target/ewm-main-service-*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]