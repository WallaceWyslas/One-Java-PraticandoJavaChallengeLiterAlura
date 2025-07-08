package br.com.literalura.literalura;

import br.com.literalura.literalura.model.Author;
import br.com.literalura.literalura.model.Book;
import br.com.literalura.literalura.model.GutendexResponse;
import br.com.literalura.literalura.repository.AuthorRepository;
import br.com.literalura.literalura.repository.BookRepository;
import br.com.literalura.literalura.service.ConsumoApi;
import br.com.literalura.literalura.service.ConverteDados;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO_BASE = "https://gutendex.com/books/";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Principal(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    *** Bem-vindo ao LiterAlura ***
                    
                    Escolha uma das opções abaixo:
                    1 - Buscar livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    System.out.println("Ainda não criada");
                    break;
                case 5:
                    System.out.println("Ainda não criada");
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void buscarLivroPeloTitulo() {
        System.out.println("Digite o nome do livro que você deseja buscar:");
        var nomeLivro = leitura.nextLine();

        Optional<Book> livroExistente = bookRepository.findByTitleContainsIgnoreCase(nomeLivro);

        if (livroExistente.isPresent()) {
            System.out.println("Este livro já está cadastrado no banco de dados.");
            System.out.println(livroExistente.get());
            return;
        }

        var json = consumo.obterDados(ENDERECO_BASE + "?search=" + nomeLivro.replace(" ", "+"));
        GutendexResponse dados = conversor.obterDados(json, GutendexResponse.class);

        Optional<Book> livroBuscado = dados.getResults().stream().findFirst();

        if (livroBuscado.isPresent()) {
            Book livro = livroBuscado.get();

            Author autorDoLivro = livro.getAuthor();
            if (autorDoLivro != null) {
                Optional<Author> autorExistente = authorRepository.findByNameContainsIgnoreCase(autorDoLivro.getName());

                if (autorExistente.isPresent()) {
                    livro.setAuthor(autorExistente.get());
                } else {
                }
            }

            bookRepository.save(livro);
            System.out.println("Livro salvo com sucesso!");
            System.out.println(livro);
        } else {
            System.out.println("Nenhum livro encontrado com esse título na API.");
        }
    }

    private void listarLivrosRegistrados() {
        List<Book> livros = bookRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado encontrado.");
        } else {
            System.out.println("----- LIVROS REGISTRADOS -----");
            livros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Author> autores = authorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado encontrado.");
        } else {
            System.out.println("----- AUTORES REGISTRADOS -----");
            autores.forEach(System.out::println);
        }
    }
}
