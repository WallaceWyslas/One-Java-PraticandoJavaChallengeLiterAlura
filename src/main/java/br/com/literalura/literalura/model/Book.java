package br.com.literalura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

/**
 * Entidade JPA que representa um livro.
 * Usada para mapear dados da API Gutendex e para persistência no banco de dados.
 */
@Entity
@Table(name = "livros")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    @JsonIgnore // O ID interno do nosso banco não deve ser enviado ou recebido via JSON.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // Garante que não teremos livros com títulos duplicados.
    private String title;

    /**
     * Relacionamento muitos-para-um com a entidade Author.
     * Muitos livros podem pertencer a um único autor.
     * - cascade: Garante que, ao salvar um livro, seu autor associado também seja salvo (se for novo).
     */
    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    private String language;

    @JsonAlias("download_count")
    private Integer downloadCount;

    @JsonAlias("id") // Mapeia o ID do livro na API Gutendex para este campo.
    private Integer gutendexId;

    public Book() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * Método auxiliar usado pelo Jackson para desserializar a lista de autores da API.
     * Conforme o requisito, pegamos apenas o primeiro autor da lista.
     * @param authors Lista de autores recebida do JSON da API.
     */
    @JsonAlias("authors")
    public void setAuthorsFromAPI(List<Author> authors) {
        if (authors != null && !authors.isEmpty()) {
            this.author = authors.get(0); // Pega apenas o primeiro autor.
        } else {
            this.author = null;
        }
    }

    public String getLanguage() {
        return language;
    }

    /**
     * Método auxiliar usado pelo Jackson para desserializar a lista de idiomas da API.
     * Conforme o requisito, pegamos apenas o primeiro idioma da lista.
     * @param languages Lista de idiomas recebida do JSON da API.
     */
    @JsonAlias("languages")
    public void setLanguages(List<String> languages) {
        if (languages != null && !languages.isEmpty()) {
            this.language = languages.get(0); // Pega apenas o primeiro idioma.
        } else {
            this.language = "desconhecido";
        }
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Integer getGutendexId() {
        return gutendexId;
    }

    public void setGutendexId(Integer gutendexId) {
        this.gutendexId = gutendexId;
    }

    /**
     * Sobrescreve o método toString para uma representação textual clara do livro.
     * @return String formatada com os dados do livro.
     */
    @Override
    public String toString() {
        String nomeAutor = (author != null) ? author.getName() : "Autor desconhecido";
        return "----- LIVRO -----" +
                "\nTítulo: " + title +
                "\nAutor: " + nomeAutor +
                "\nIdioma: " + language +
                "\nNúmero de Downloads: " + downloadCount +
                "\n-----------------\n";
    }
}