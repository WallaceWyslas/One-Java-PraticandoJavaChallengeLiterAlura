package br.com.literalura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entidade JPA que representa um autor.
 * Esta classe é usada tanto para mapear os dados da API Gutendex
 * quanto para persistir as informações do autor no banco de dados.
 */
@Entity
@Table(name = "autores")
@JsonIgnoreProperties(ignoreUnknown = true) // Ignora campos do JSON que não estão mapeados nesta classe
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária do autor no banco de dados

    @Column(unique = true) // Garante que não haverá autores com o mesmo nome
    private String name;

    @JsonAlias("birth_year") // Mapeia o campo "birth_year" do JSON para este atributo
    private Integer birthYear;

    @JsonAlias("death_year") // Mapeia o campo "death_year" do JSON para este atributo
    private Integer deathYear;

    /**
     * Relacionamento um-para-muitos com a entidade Book.
     * Um autor pode ter escrito vários livros.
     * - mappedBy: Indica que a entidade Book é a dona do relacionamento (ela contém a foreign key).
     * - cascade: Propaga todas as operações (salvar, atualizar, deletar) do Autor para seus Livros associados.
     * - fetch: EAGER carrega os livros do autor imediatamente quando o autor é carregado do banco.
     */
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    /**
     * Construtor padrão sem argumentos.
     * Exigido pelo JPA para a criação de instâncias da entidade.
     */
    public Author() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Sobrescreve o método toString para fornecer uma representação textual clara do autor,
     * incluindo uma lista dos títulos de seus livros.
     *
     * @return String formatada com os dados do autor.
     */
    @Override
    public String toString() {
        // Usa Stream API para coletar os títulos de todos os livros associados e juntá-los em uma única String.
        String titulosLivros = books.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "));

        return "----- AUTOR -----" +
                "\nNome: " + name +
                "\nAno de Nascimento: " + birthYear +
                "\nAno de Falecimento: " + deathYear +
                "\nLivros: [" + titulosLivros + "]" +
                "\n-----------------\n";
    }
}