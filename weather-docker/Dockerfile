FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk update && apk upgrade && mvn clean package -DskipTests

FROM eclipse-temurin:17.0.11_9-jre-alpine
WORKDIR /app
RUN apk update && apk upgrade
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]