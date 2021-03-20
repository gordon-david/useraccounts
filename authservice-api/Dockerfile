# Build Step
#from openjdk:11 as build
#workdir workspace/app
#
#copy mvnw .
#copy .mvn .mvn
#copy pom.xml .
#copy src src
#
#run ./mvnw install -DskipTests
#
#from openjdk:11 as build
#workdir workspace/app
#
#copy mvnw .
#copy .mvn .mvn
#copy pom.xml .
#copy src src
#
#run ./mvnw install -DskipTests
FROM maven:latest as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -Dskiptests

FROM openjdk:11
COPY --from=builder /app/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]


