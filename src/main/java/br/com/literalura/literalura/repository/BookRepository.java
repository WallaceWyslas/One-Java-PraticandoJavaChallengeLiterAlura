package br.com.literalura.literalura.repository;

import br.com.literalura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório do Spring Data JPA para a entidade Book.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Busca um livro pelo título, ignorando maiúsculas e minúsculas.
     * @param title Título (ou parte do título) do livro.
     * @return Um Optional contendo o livro encontrado.
     */
    Optional<Book> findByTitleContainsIgnoreCase(String title);

    /**
     * Busca todos os livros registrados em um determinado idioma.
     * @param language O código do idioma (ex: "en", "pt").
     * @return Uma lista de livros para o idioma especificado.
     */
    List<Book> findByLanguage(String language);
}