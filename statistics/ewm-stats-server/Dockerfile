# ===== Build stage =====
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY statistics/ewm-stats-dto/pom.xml statistics/ewm-stats-dto/
COPY statistics/ewm-stats-server/pom.xml statistics/ewm-stats-server/

RUN mvn -B -q -DskipTests -pl statistics/ewm-stats-server -am dependency:go-offline

COPY . .
RUN mvn -B -q -DskipTests -pl statistics/ewm-stats-server -am package

# ===== Runtime stage =====
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/statistics/ewm-stats-server/target/ewm-stats-server-*.jar /app/app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","/app/app.jar"]