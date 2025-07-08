# LiterAlura 📚

LiterAlura é um catálogo de livros interativo que funciona via console. Este projeto foi desenvolvido como um desafio de programação da Oracle, junto à Alura para praticar o consumo de APIs, manipulação de dados com Java e persistência com Spring Data JPA.

A aplicação consome a API pública [Gutendex](https://gutendex.com/) para buscar informações sobre livros e autores, e salva os dados em um banco de dados PostgreSQL para consultas futuras.

## ✨ Funcionalidades

*   **Buscar Livro por Título:** Busca um livro na API Gutendex e o salva no banco de dados.
*   **Listar Livros Registrados:** Exibe todos os livros salvos no banco de dados.
*   **Listar Autores Registrados:** Exibe todos os autores salvos.
*   **Listar Autores Vivos:** Mostra os autores que estavam vivos em um determinado ano.
*   **Listar Livros por Idioma:** Filtra e exibe os livros registrados em um idioma específico (inglês, português, espanhol, francês).
*   **Top 10 Livros:** Busca os 10 livros mais populares na API e os salva localmente.
*   **Estatísticas:** Exibe dados estatísticos sobre os livros da coleção, como média de downloads.

## 🛠️ Tecnologias Utilizadas

*   **Java 17**
*   **Spring Boot 3**
*   **Spring Data JPA**
*   **PostgreSQL** (Banco de Dados)
*   **Maven** (Gerenciador de Dependências)
*   **Jackson** (Manipulação de JSON)
*   **API Gutendex** (Fonte dos dados)

## 🚀 Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/WallaceWyslas/One-Java-PraticandoJavaChallengeLiterAlura.git
    ```

2.  **Configure o Banco de Dados:**
    *   Crie um banco de dados PostgreSQL.
    *   Abra o arquivo `src/main/resources/application.properties`.
    *   Altere as propriedades `spring.datasource.url`, `spring.datasource.username` e `spring.datasource.password` com as suas credenciais do PostgreSQL.

3.  **Execute a Aplicação:**
    *   Abra o projeto em sua IDE de preferência (IntelliJ, VS Code, Eclipse).
    *   Execute a classe principal `LiteraluraApplication.java`.
    *   A aplicação iniciará e o menu interativo será exibido no console.