# Intruções para Executar o Projeto

**Observação**: Todos os comandos descritos consideram a sua execução através de um sistema operacional Linux ou MacOS.
Para executar no Windows, é necessário fazer as adaptações dos comandos ao SO.

## Pré-requisitos

- Docker
- docker-compose
- Maven
- JDK 1.8+
- IntelliJ ou Eclipse IDE

## Instanciando o PostgreSQL

Na pasta raíz do projeto, execute através de um terminal:
`
cd ./docker && docker-compose up
`

## Inicializando o servidor Tomcat na porta 8080

**Observação**: Antes de inicializar o servidor, configure o diretório onde as extrações das páginas PDF serão
armazenadas, alterando o valor da propriedade `pdf.pages.dir` no arquivo `application.properties` (**tem que ser um 
diretório raíz existente e acessível**)

Na pasta raíz do projeto, execute através de um terminal:
`
mvn spring-boot:run
`

### Caso precisem analisar os logs das instruções SQL executadas

- No arquivo `application.properties` da aplicação descomente a linha `# logging.level.org.hibernate.type.descriptor.sql=trace`
- No arquivo `application.properties` da aplicação altere a linha `spring.jpa.show-sql=false` para `spring.jpa.show-sql=true`
- Para confirmar as instruções registradas no 

## Executando os Endpoints de Extração/Consulta

- Acesse a URL `http://localhost:8080/swagger-ui/#/pdf-page-controller`
- Através do endpoint com método POST, clique na opção **Try it out** e na sequência no botão **Execute**
- Visualize o **Response Body** retornado e os ___logs___ de processamento no terminal do Tomcat
- Através do endpoint com método GET, clique na opção **Try it out** e na sequência no botão **Execute**
- Visualize o **Response Body** retornado
- Acesso o caminho do diretório configurado no `pdf.pages.dir` e visualize os PDF extraídos

_Good luck!_
