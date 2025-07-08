package br.com.literalura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "livros")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    private Author author;

    private String language;

    @JsonAlias("download_count")
    private Integer downloadCount;

    @JsonAlias("id")
    private Integer gutendexId;

    // Construtor padrão
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

    // ****** ADICIONE ESTE MÉTODO ******
    public void setAuthor(Author author) {
        this.author = author;
    }
    // **********************************

    // Este método continua aqui para uso do Jackson
    @JsonAlias("authors")
    public void setAuthorsFromAPI(List<Author> authors) {
        if (authors != null && !authors.isEmpty()) {
            this.author = authors.get(0);
        } else {
            this.author = null;
        }
    }

    public String getLanguage() {
        return language;
    }

    // Este método continua aqui para uso do Jackson
    @JsonAlias("languages")
    public void setLanguages(List<String> languages) {
        if (languages != null && !languages.isEmpty()) {
            this.language = languages.get(0);
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