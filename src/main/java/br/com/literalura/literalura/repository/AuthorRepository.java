package br.com.literalura.literalura.repository;

import br.com.literalura.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório do Spring Data JPA para a entidade Author.
 * Fornece métodos CRUD (Create, Read, Update, Delete) prontos para uso
 * e permite a declaração de consultas customizadas.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Busca um autor pelo nome, ignorando maiúsculas e minúsculas.
     * Esta é uma "derived query", onde o Spring Data JPA cria a consulta automaticamente a partir do nome do método.
     * @param name Nome (ou parte do nome) do autor a ser buscado.
     * @return Um Optional contendo o autor encontrado, ou vazio se não encontrar.
     */
    Optional<Author> findByNameContainsIgnoreCase(String name);

    /**
     * Busca autores que estavam vivos em um determinado ano.
     * A lógica considera autores que nasceram antes ou no ano fornecido, e que faleceram
     * depois ou no ano fornecido (ou que ainda não faleceram).
     * @param ano O ano para a verificação.
     * @return Uma lista de autores que estavam vivos no ano especificado.
     */
    @Query("SELECT a FROM Author a WHERE a.birthYear <= :ano AND (a.deathYear IS NULL OR a.deathYear >= :ano)")
    List<Author> findAutoresVivosEmAno(Integer ano);
}