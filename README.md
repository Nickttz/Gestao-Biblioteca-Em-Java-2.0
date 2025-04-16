# 📚 Sistema de Gestão de Biblioteca com Java e Spring Boot 

Este projeto tem como objetivo desenvolver um sistema completo de gerenciamento de bibliotecas, com controle de usuários, livros e empréstimos. O sistema está sendo construído com foco em **segurança, escalabilidade e usabilidade**.

### ⚠️OBS: Não foram muitos testes realizados, portanto pode haver exceções não tratadas.

---

## ✅ Funcionalidades

- 🔐 **Login seguro com autenticação JWT**
- 👤 **Cadastro de usuários leitores**
- 📚 **Cadastro, listagem, edição e remoção de livros**
- 📖 **Empréstimo e devolução de livros com controle de prazos**
- ⏰ **Validação de disponibilidade e datas de devolução**
- 🌐 **Interface web amigável (HTML/CSS/Bootstrap)**
- 🔌 **API REST pronta para integração com outras plataformas**
- 🧪 **Testes de API com Postman**

---

## ⚙️ Tecnologias Utilizadas

| Camada          | Tecnologia                                      |
|-----------------|-------------------------------------------------|
| Backend         | Java, Spring Boot                               |
| Autenticação    | JWT (JSON Web Token)                            |
| Banco de Dados  | PostgreSQL (com Docker)                         |
| Frontend        | HTML, CSS, JavaScript, Bootstrap                |
| API             | RESTful                                         |
| Testes          | Postman                                         |
| Gerenciamento   | Maven                                           |
| Ambiente Dev    | Docker, Docker Compose                          |

---

## 🐳 Banco de Dados com Docker

O banco de dados é executado dentro de containers Docker, facilitando a replicação e o desenvolvimento local. Ele inclui:

- **PostgreSQL:** Sistema gerenciador de banco de dados relacional.
- **PgAdmin:** Interface gráfica para administrar o banco.
- **Rede personalizada:** Comunicação do tipo bridge entre os containers.

### 📄 Exemplo de `docker-compose.yml`:

```yaml
version: '3.8'

services:
  db:
    image: postgres:16
    restart: always
    container_name: biblioteca_db
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: biblioteca
    ports:
      - "5432:5432"
    networks:
      - biblioteca-network
    volumes:
      - postgres_data:/var/lib/postgresql/data

  pgadmin:
    image: dpage/pgadmin4
    restart: always
    container_name: biblioteca_pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "5050:80"
    networks:
      - biblioteca-network

networks:
  biblioteca-network:
    driver: bridge

volumes:
  postgres_data:
````
## 🚀 Executando o Projeto

### 1. Clone o repositório:
```
git clone https://github.com/seuusuario/sistema-biblioteca.git
cd sistema-biblioteca
```

### 2. Suba o banco de dados com Docker:
```
docker-compose up -d
```
### 3. Compile e execute o projeto: 
```
mvn clean install
mvn spring-boot:run
```
