# API de Cotação de Seguro 📝

## Descrição 📝

Esta API RESTful, desenvolvida em Spring Boot, tem como objetivo gerenciar cotações de seguro 💼, permitindo a criação de novas cotações 🆕, consulta de cotações existentes 🔎 e integração com APIs externas 🔗, como um serviço de catálogo de produtos e ofertas de seguros. Além disso, a aplicação utiliza filas SQS ➡️ para comunicação assíncrona entre diferentes componentes e sistemas.

## Funcionalidades Principais:

* Criar cotação de seguro: Permite a criação de novas cotações de seguro com informações detalhadas sobre o cliente 🧑‍💼, produto 📦, oferta 🏷️ e coberturas desejadas. Ao criar uma cotação, uma mensagem é publicada na fila insurence-quote-received.fifo 📩 para notificar outros sistemas sobre a nova cotação.
* Consultar cotação de seguro por ID: Possibilita a consulta de cotações existentes 🔍 por meio de um ID único 🔢.distribuição.
* Integrar com API de Catálogo: Integra-se com uma API externa de catálogo 🔗 para validar a existência e a disponibilidade de produtos e ofertas de seguros.
* Processar resposta da criação da apólice: A aplicação possui um consumer que escuta a fila insurance-policy-created 👂 e processa as mensagens recebidas, que contêm informações sobre a apólice de seguro criada. Quando uma mensagem é consumida, a cotação correspondente é atualizada com o número da apólice.

## Construindo uma Aplicação Resiliente e Escalável: 🚀

Para garantir que a API de Cotação de Seguro seja robusta 💪 e funcione de forma confiável ⏱️, mesmo em situações de alto tráfego 📈 ou falhas em serviços externos ⚠️, foram utilizadas bibliotecas que fornecem mecanismos de resiliência e tolerância a falhas, como Circuit Breaker 🔌.

## Mapeamento de Objetos e Documentação da API:

Para facilitar o desenvolvimento 💻 e a manutenção 🔧 da aplicação, foram utilizadas bibliotecas que auxiliam no mapeamento de objetos 🔀 e na geração de documentação da API 📑.

## Integração com a Nuvem AWS:

A aplicação foi projetada para ser executada na nuvem AWS ☁️ e utiliza serviços como o DynamoDB 📊 e o SQS ➡️ para garantir escalabilidade e performance 🚀.

* DynamoDB: Um banco de dados NoSQL gerenciado pela AWS, utilizado para armazenar as cotações de seguro. Essa escolha garante alta escalabilidade, performance e disponibilidade, ideal para lidar com um grande volume de dados e acessos concorrentes.

* Amazon SQS: Um serviço de filas de mensagens que permite desacoplar e escalar microsserviços, sistemas distribuídos e aplicações. Com o SQS, a aplicação pode enviar e receber mensagens de forma assíncrona, o que aumenta a confiabilidade e a escalabilidade do sistema. As filas SQS atuam como buffers temporários para as mensagens, que podem ser processadas posteriormente por outros componentes da aplicação ou por sistemas externos. Isso permite que diferentes partes da aplicação se comuniquem de forma eficiente, sem precisar estar online ao mesmo tempo.

## Tecnologias Utilizadas 💻

* Linguagem de programação: Java (versão 17)
* Framework web: Spring Boot (versão 3.4.1)
* Gerenciador de dependências: Gradle (versão 8.11.1)
* Banco de dados: Amazon DynamoDB
* Fila de mensageria: Amazon SQS
* Containerização: Docker
* Orquestração: Kubernetes
* Outras tecnologias:
  * Swagger (para documentação da API)
  * AWS SDK for Java (para integração com o DynamoDB e Secrets Manager)
  * MapStruct (para mapeamento de objetos)
  * Lombok (para simplificar o código)

## Instalação 🔧

1. Clone o repositório: `git clone https://github.com/leovieir4/insurance-policy-quote-itau`
2. Navegue até o diretório do projeto: `cd insurance-policy-quote-itau`

## 🚀 Deploy na AWS:
[Link da API](http://13.59.156.55)

## Execução ▶️

### Localmente com Docker

1. Construa a imagem Docker: `docker build -t insurance-policy-quote-itau .`
2. Execute o container Docker: `docker run -p 8080:8080 insurance-policy-quote-itau`
3. A API estará disponível em: `http://localhost:8080` (ou em outra porta que você tenha configurado)
4. A documentação da API estará disponível em: `http://localhost:8080/swagger-ui/index.html`

### Em um cluster Kubernetes

1. Crie o segredo JWT: `kubectl apply -f k8s/secret-jwt.yaml`
2. Faça o deploy da aplicação: `kubectl apply -f k8s/deployment.yaml`
3. Exponha o serviço: `kubectl apply -f k8s/service.yaml`

### Localmente com Gradle 🔨

1. **Limpe e construa o projeto:** `./gradlew clean build`
2. **Execute a aplicação:** `./gradlew bootRun`
3. A API estará disponível em: `http://localhost:8080` (ou em outra porta que você tenha configurado)
4. A documentação da API estará disponível em: `http://localhost:8080/swagger-ui/index.html`


## Gradle 🐘

O projeto utiliza o Gradle como gerenciador de dependências e ferramenta de build. O arquivo `build.gradle` define as dependências do projeto, plugins e tarefas para construir, testar e executar a aplicação.

Para construir o projeto, execute o comando `./gradlew build`.

Para executar a aplicação localmente, execute o comando `./gradlew bootRun`.

## Docker 🐳

O projeto utiliza um Dockerfile de múltiplos estágios para construir e executar a aplicação.

* **Estágio de build:**
  * Utiliza a imagem `ubuntu:latest` como base.
  * Instala o Java JDK 17, unzip e wget.
  * Faz o download e instala o Gradle 8.11.1
  * Copia o código-fonte da aplicação.
  * Executa o comando `gradle clean build` para construir o projeto.
* **Estágio de runtime:**
  * Utiliza a imagem `ubuntu:latest` como base.
  * Instala o Java JRE 17.
  * Copia o arquivo JAR gerado no estágio de build.
  * Define as permissões do arquivo JAR.
  * Copia as credenciais da AWS (se necessário).
  * Expõe a porta 8080.
  * Define o comando de entrada para executar a aplicação com o perfil `not_local`.

## Kubernetes 🚢

A pasta `k8s` contém os arquivos de configuração para deploy da aplicação em um cluster Kubernetes:

* **`deployment.yaml`:** Define o deployment da aplicação, incluindo a imagem Docker, número de réplicas, portas, variáveis de ambiente e recursos.
* **`service.yaml`:** Define o serviço Kubernetes para expor a aplicação internamente ou externamente.
* **`secret-jwt.yaml`:** Define o segredo Kubernetes para armazenar a chave secreta utilizada para assinar o JWT.

## Testes 🧪

A aplicação conta com uma suíte de testes automatizados para garantir a qualidade e o bom funcionamento dos principais fluxos da API. Abaixo, estão descritos os detalhes dos testes utilizados e como rodá-los localmente.

### Estrutura de Testes

A aplicação utiliza o framework de testes **JUnit 5** para escrever testes unitários e de integração. Para os testes de integração, é utilizado o **Spring Boot Test** para simular o comportamento da aplicação com o contexto do Spring.

Além disso, para testes de comportamento e validação de requisições HTTP, é utilizado o **MockMvc** para simular as requisições e validar as respostas da API.

### Testes Unitários

Os testes unitários são escritos para testar a lógica de negócios isolada, sem a necessidade de interagir com componentes externos como bancos de dados ou filas de mensagens. Os principais componentes testados incluem:

- **Controllers**: Verificação de que as respostas HTTP estão corretas.
- **Use Cases**: Teste da lógica de negócio.
- **Repositórios**: Testes com mocks para garantir que a lógica de acesso ao banco de dados está correta.

### Testes de Integração

Os testes de integração visam verificar a integração entre os componentes da aplicação. Eles são realizados com o **Spring Boot Test**, que cria um contexto de aplicação real para garantir que os fluxos de dados entre as camadas funcionem como esperado.

Para simular os serviços da AWS (como **DynamoDB**, **SQS** e outros), utilizamos **LocalStack** durante os testes de integração. O **LocalStack** simula os serviços AWS localmente, permitindo a execução de testes sem a necessidade de acesso real à nuvem, o que aumenta a confiabilidade e a rapidez dos testes.

### Como Executar os Testes Localmente

Para executar os testes localmente, basta executar o seguinte comando no diretório do projeto:


# Para executar todos os testes
**`./gradlew test`**

Obs: para executar os testes de integração o docker deve estar ativo


## Diagramas C4 Model 📈


* **Criação de cotações:**

  ![Criação de cotações](https://i.ibb.co/h2ZFRYP/Captura-de-tela-2025-01-13-195449.png)

* **Consultar cotação**

  ![Consultar cotação](https://i.ibb.co/X41TtmP/Captura-de-tela-2025-01-13-194859.png)

* **Atualizar cotação:**

  ![Atualizar contação](https://i.ibb.co/G2gd7vx/Captura-de-tela-2025-01-13-200115.png)


## Endpoints da API 🌐

* `POST /insurance-policies`: Criar cotação.
  * Request Body:
    ```json
    {
    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
    "category": "HOME",
    "total_monthly_premium_amount": 75.25,
    "total_coverage_amount": 825000,
    "coverages": {
        "Incêndio": 250000,
        "Desastres naturais": 500000,
        "Responsabiliadade civil": 75000
    },
    "assistances": [
        "Encanador",
        "Eletricista",
        "Chaveiro 24h"
    ],
    "customer": {
        "document_number": "36205578900",
        "name": "John Wick",
        "type": "NATURAL",
        "gender": "MALE",
        "date_of_birth": "1973-05-02",
        "email": "johnwick@gmail.com",
        "phone_number": 11950503030
    }
}
* Response Codes:

        * `201 Created`: Cotação criado com sucesso.
        * `400 Bad Request`: Requisição inválida.
        * `500 Internal Server Error`: Erro interno do servidor.

* `GET /insurance-policies/{id}`: Consultar uma cotação pelo ID.
  * Path Variables:
    * `id`: ID da cotação.
  * Response Codes:
    * `200 OK`: Cotação encontrado com sucesso.
    * `404 Not Found`: Cotação não encontrado.
    * `500 Internal Server Error`: Erro interno do servidor.

## Exemplo de Uso ⌨️

**Criar uma nova cotação:**

```bash
curl --location 'http://localhost:8080/insurance-policies' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=51280F7F9800604B98ED18F8193B73F4' \
--data-raw '{
    "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587",
    "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c",
    "category": "HOME",
    "total_monthly_premium_amount": 75.25,
    "total_coverage_amount": 825000,
    "coverages": {
        "Incêndio": 250000,
        "Desastres naturais": 500000,
        "Responsabiliadade civil": 75000
    },
    "assistances": [
        "Encanador",
        "Eletricista",
        "Chaveiro 24h"
    ],
    "customer": {
        "document_number": "36205578900",
        "name": "John Wick",
        "type": "NATURAL",
        "gender": "MALE",
        "date_of_birth": "1973-05-02",
        "email": "johnwick@gmail.com",
        "phone_number": 11950503030
    }
}'
```

**Consultar cotação:**

```bash
curl --location --globoff 'http://localhost:8080/insurance-policies/{id}' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=51280F7F9800604B98ED18F8193B73F4'
```

## 📖 Documentação da API (Swagger):
[Documentação](http://13.59.156.55:8080/swagger-ui/index.html)

## 🚀 Deploy na AWS:
[Link da API](http://13.59.156.55:8080)

## 🧑‍💻 Autor:
Leonardo Vieira da Silva

## 📞 Contato:

Email: vieraleonardosilva@gmail.com

Telefone: (11) 994419472