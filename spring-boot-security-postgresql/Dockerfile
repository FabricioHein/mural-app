# Etapa de Build
FROM ubuntu:latest AS build

# Defina uma variável de ambiente para evitar prompts interativos durante a instalação
ENV DEBIAN_FRONTEND=noninteractive

# Atualize os pacotes, instale dependências necessárias e limpe caches
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Defina o diretório de trabalho no contêiner de build
WORKDIR /spring-boot-security-postgresql

# Copie o arquivo pom.xml e baixe as dependências para aproveitar o cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Agora copie o restante do projeto
COPY . .

# Construa o projeto (evitando os testes)
RUN mvn clean package -DskipTests

# Etapa Final (Imagem leve para execução)
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho no contêiner final
WORKDIR /spring-boot-security-postgresql

# Exponha a porta do Spring Boot
EXPOSE 8080

# Copie o JAR construído na etapa de build
COPY --from=build /spring-boot-security-postgresql/target/*.jar app.jar

# Defina o comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
