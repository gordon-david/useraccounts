FROM maven:latest as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -Dskiptests

FROM openjdk:11
COPY --from=builder /app/target/*.jar /app.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/apidb
ENV SPRING_DATASOURCE_USERNAME=apiuser
ENV SPRING_DATASOURCE_PASSWORD=apiuserpassword
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
