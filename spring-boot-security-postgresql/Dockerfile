FROM maven:3.9-amazoncorretto-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Adiciona flag --fail-at-end para ver todos os erros
# E -e para mostrar os logs de erro completos
RUN mvn clean package --fail-at-end -e -X

FROM openjdk:17-jdk-slim

WORKDIR /app
EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]