# Etapa de Build
FROM ubuntu:latest AS build

# Evitar prompts interativos durante a instalação
ENV DEBIAN_FRONTEND=noninteractive

# Atualize os pacotes e instale o OpenJDK e Maven
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

# Defina o diretório de trabalho no contêiner de build
WORKDIR /app

# Copie apenas o arquivo pom.xml para aproveitar o cache das dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copie o restante dos arquivos do projeto
COPY . .

# Construa o projeto (ignorar testes para builds mais rápidos)
RUN mvn clean install -DskipTests

# Etapa Final (Imagem leve para execução)
FROM openjdk:21-jdk-slim

# Defina o diretório de trabalho no contêiner final
WORKDIR /app

# Exponha a porta usada pelo Spring Boot
EXPOSE 8080

# Copie o JAR gerado na etapa de build para a imagem final
COPY --from=build /app/target/*.jar app.jar

# Defina o comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
