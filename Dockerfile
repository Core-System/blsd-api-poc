FROM maven:3.9.11-amazoncorretto-21 AS deps

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline -B

FROM deps AS build

COPY src ./src

RUN mvn package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]