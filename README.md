
# Calculadora de Impostos API


API para cálculo e gerenciamento de impostos no Brasil. Este projeto foi desenvolvido utilizando Spring Boot, com autenticação baseada em JWT e documentação de endpoints com Swagger.



## Índice

1. Pré-requisitos
2. Configuração do Ambiente
3. Instalação
4. Execução
5. Documentação da API
6. Testes
7. Estrutura do Projeto
8. Endpoints Principais
9. Contribuição
## Pré-requisitos

Antes de começar, certifique-se de que você possui os seguintes itens instalados no seu ambiente:

1.Java 17 ou superior
2.Maven 3.8+
3.Banco de Dados MySQL (ou outro banco compatível configurado no application.properties)
4.Postman ou cURL (opcional, para testar os endpoints)
5.(para clonar o repositório)

## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente no seu .env

`API_KEY`

`ANOTHER_API_KEY`


## Configuração do Ambiente

### Banco de Dados

1. Crie um banco de dados no MySQL com o nome calculadora_impostos:

```bash
  CREATE DATABASE calculadora_impostos;
```

2. Configure as credenciais do banco no arquivo src/main/resources/application.properties:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/calculadora_impostos
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

##JWT
Certifique-se de que a chave secreta para geração de tokens JWT está configurada no arquivo application.properties:

```bash
jwt.secret=chave_secreta_segura
jwt.expiration=3600000 //Tempo de espiração
```
## Instalação

1. Clone o repositório:

```bash
  git clone https://github.com/seu-usuario/calculadora-impostos.git
  cd calculadora-impostos
```

2. Compile o projeto e baixe as dependências:

```bash
mvn clean install
```
## Execução

1. Inicie a aplicação:

```bash
mvn spring-boot:run
```
2. A aplicação estará disponível em:

http://localhost:8080
## Documentação

A documentação da API está disponível no Swagger. Para acessá-la, inicie a aplicação e abra o seguinte link no navegador:

[Documentação](http://localhost:8080/swagger-ui.html)

### Exemplo de Endpoints Documentados
GET /tipos - Lista todos os tipos de impostos   
POST /tipos - Cadastra um novo tipo de imposto   
POST /user/login - Autentica um usuário e retorna um token JWT  
## Testes
### Testes Unitários
Os testes unitários estão localizados no diretório src/test/java. Para executá-los, utilize o comando:

```bash
  mvn test
```


## Estrutura do Projeto

calculadora-impostos/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com.catalisa.calculadoraImposto/   
│   │   │   │   ├── config/         # Configurações (Swagger, Security, etc.)   
│   │   │   │   ├── controller/     # Controladores REST   
│   │   │   │   ├── dto/            # Objetos de Transferência de Dados   
│   │   │   │   ├── exception/      # Tratamento de exceções   
│   │   │   │   ├── model/          # Modelos de Entidade   
│   │   │   │   ├── repository/     # Repositórios JPA   
│   │   │   │   ├── service/        # Lógica de Negócio   
│   │   │   │   └── CalculadoraImpostoApplication.java # Classe principal   
│   │   ├── resources/   
│   │   │   ├── application.properties # Configurações da aplicação   
│   │   │   └── data.sql               # Dados iniciais (opcional)   
│   ├── test/   
│       ├── java/                      # Testes unitários  
├── pom.xml                            # Arquivo de dependências Maven   
└── README.md                          # Documentação do projeto     

## Endpoints Principais   

### Autenticação
POST /user/login - Autentica um usuário e retorna um token JWT.
Exemplo de Requisição:


{
    "username": "admin",     
    "password": "admin123"   
}   

Resposta:

{   
 "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."   
}   

Tipos de Impostos
GET /tipos - Lista todos os tipos de impostos (requer role ADMIN).

POST /tipos - Cadastra um novo tipo de imposto (requer role ADMIN).

Exemplo de Requisição:   
   
{   
  "nome": "IPI",   
  "descricao": "Imposto sobre Produtos Industrializados",   
  "aliquota": 12.0   
}    

POST /tipos/calculo - Calcula o valor do imposto com base no tipo e valor base (requer role ADMIN).

Exemplo de Requisição:   

{   
  "tipoImpostoId": 1,   
  "valorBase": 1000.0   
}
## Contribuindo

Contribuições são sempre bem-vindas!

1. Faça um fork do repositório.   

2. Crie uma branch para sua feature:
```bash
  git checkout -b minha-feature

```
3. Faça commit das suas alterações:
```bash
  git commit -m "Adiciona minha nova feature"
```
4. Envie para o repositório remoto:
```bash
  git push origin minha-feature
```
