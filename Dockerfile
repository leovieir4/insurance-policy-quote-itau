# Stage 1: Build the application
FROM ubuntu:latest AS build
WORKDIR /app

# Atualizar e instalar o JDK 17 e outras dependências
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk wget unzip && \
    rm -rf /var/lib/apt/lists/*

# Baixar e instalar o Gradle 8.11.1
RUN wget https://services.gradle.org/distributions/gradle-8.11.1-bin.zip && \
    unzip gradle-8.11.1-bin.zip && \
    mv gradle-8.11.1 /opt/gradle && \
    ln -s /opt/gradle/bin/gradle /usr/bin/gradle

# Copiar o código fonte para o container
COPY . .

# Construir o projeto usando Gradle
RUN gradle clean build -x test

# Stage 2: Criar a imagem de runtime
FROM ubuntu:latest
WORKDIR /app

# Instalar o JDK 17
RUN apt-get update && \
    apt-get install -y openjdk-17-jre && \
    rm -rf /var/lib/apt/lists/*


# Copiar as credenciais da AWS (se necessário)
COPY credentials /root/.aws/credentials

# Copiar o JAR gerado do estágio anterior
COPY --from=build /app/build/libs/insurance-policy-itau-0.0.1-SNAPSHOT.jar app.jar

# Definir permissões para o JAR
RUN chmod 755 /app/app.jar

# Expor a porta 8080 para acesso externo e rodar a aplicação
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=not_local", "-jar", "app.jar"]
