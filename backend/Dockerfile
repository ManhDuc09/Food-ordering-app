FROM maven:3.9-eclipse-temurin-17-alpine
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
ENTRYPOINT ["mvn", "spring-boot:run"]