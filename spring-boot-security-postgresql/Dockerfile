FROM maven:3.9-amazoncorretto-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Adiciona mais verbosidade ao build do Maven
RUN mvn clean package -X

# Lista os arquivos gerados para debug
RUN ls -la target/

FROM openjdk:17-jdk-slim

WORKDIR /app
EXPOSE 8080

# Ajuste o nome do arquivo JAR conforme necessário
COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]