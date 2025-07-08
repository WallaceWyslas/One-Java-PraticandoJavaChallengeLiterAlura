package br.com.literalura.literalura;

import br.com.literalura.literalura.model.Author;
import br.com.literalura.literalura.model.Book;
import br.com.literalura.literalura.model.GutendexResponse;
import br.com.literalura.literalura.repository.AuthorRepository;
import br.com.literalura.literalura.repository.BookRepository;
import br.com.literalura.literalura.service.ConsumoApi;
import br.com.literalura.literalura.service.ConverteDados;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

/**
 * Classe principal que contém a lógica de interação com o usuário via console.
 * Orquestra as chamadas para a API, conversão de dados e persistência no banco.
 */
@Component
public class Principal {
    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO_BASE = "https://gutendex.com/books/";

    // Repositórios injetados pelo Spring para acesso ao banco de dados.
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * Construtor que recebe as dependências (repositórios) via injeção de dependência do Spring.
     */
    public Principal(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * Exibe o menu principal e processa a entrada do usuário em um loop contínuo.
     */
    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
            \n*** Bem-vindo ao LiterAlura ***
            
            Escolha uma das opções abaixo:
            1 - Buscar livro por título (API)
            2 - Listar livros registrados
            3 - Listar autores registrados
            4 - Listar autores vivos em um determinado ano
            5 - Listar livros em um determinado idioma
            6 - Buscar Top 10 livros mais populares (API)
            7 - Exibir estatísticas gerais
            
            0 - Sair
            """;

            System.out.println(menu);
            try {
                opcao = leitura.nextInt();
                leitura.nextLine(); // Consome a nova linha pendente
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, digite um número válido.");
                leitura.nextLine(); // Limpa o buffer do scanner
                opcao = -1; // Reseta a opção para continuar no loop
                continue;
            }

            switch (opcao) {
                case 1:
                    buscarLivroNaApi();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEmDeterminadoAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 6:
                    buscarTop10Livros();
                    break;
                case 7:
                    exibirEstatisticasGerais();
                    break;
                case 0:
                    System.out.println("Saindo do LiterAlura...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    /**
     * Busca um livro na API Gutendex com base no título fornecido pelo usuário.
     * Se o livro for encontrado, verifica se ele já existe no banco. Se não existir,
     * associa o autor (buscando ou criando um novo) e salva o livro.
     */
    private void buscarLivroNaApi() {
        System.out.println("Digite um título ou nome de autor para buscar:");
        var buscaUsuario = leitura.nextLine();

        // Normaliza e codifica a busca para ser usada na URL.
        String buscaNormalizada = Normalizer.normalize(buscaUsuario, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        String enderecoBusca = ENDERECO_BASE + "?search=" + URLEncoder.encode(buscaNormalizada, StandardCharsets.UTF_8);

        try {
            var json = consumo.obterDados(enderecoBusca);
            GutendexResponse dados = conversor.obterDados(json, GutendexResponse.class);
            Optional<Book> livroBuscado = dados.getResults().stream().findFirst();

            if (livroBuscado.isPresent()) {
                Book livro = livroBuscado.get();

                // Verifica se o livro já existe no banco de dados para evitar duplicatas.
                Optional<Book> livroExistente = bookRepository.findByTitleContainsIgnoreCase(livro.getTitle());
                if (livroExistente.isPresent()) {
                    System.out.println("O livro '" + livro.getTitle() + "' já está cadastrado no banco de dados.");
                    return;
                }

                // Verifica se o autor do livro já existe no banco. Se sim, usa o autor existente. Senão, usa o novo.
                Author autorDoLivro = livro.getAuthor();
                if (autorDoLivro != null) {
                    Optional<Author> autorExistente = authorRepository.findByNameContainsIgnoreCase(autorDoLivro.getName());
                    livro.setAuthor(autorExistente.orElse(autorDoLivro));
                }

                bookRepository.save(livro); // Salva o livro (e o autor, em cascata, se for novo).
                System.out.println("Livro encontrado e salvo com sucesso!");
                System.out.println(livro);

            } else {
                System.out.println("Nenhum livro encontrado para o termo '" + buscaUsuario + "'.");
            }
        } catch (Exception e) {
            System.out.println("Ocorreu um erro durante a busca: " + e.getMessage());
        }
    }

    /**
     * Lista todos os livros que estão registrados no banco de dados.
     */
    private void listarLivrosRegistrados() {
        List<Book> livros = bookRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado encontrado.");
        } else {
            System.out.println("----- LIVROS REGISTRADOS -----");
            livros.forEach(System.out::println);
        }
    }

    /**
     * Lista todos os autores que estão registrados no banco de dados.
     */
    private void listarAutoresRegistrados() {
        List<Author> autores = authorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado encontrado.");
        } else {
            System.out.println("----- AUTORES REGISTRADOS -----");
            autores.forEach(System.out::println);
        }
    }

    /**
     * Lista os autores que estavam vivos em um ano específico fornecido pelo usuário.
     */
    private void listarAutoresVivosEmDeterminadoAno() {
        System.out.println("Digite o ano para pesquisar os autores vivos:");
        try {
            var ano = leitura.nextInt();
            leitura.nextLine(); // Consome a nova linha

            List<Author> autoresVivos = authorRepository.findAutoresVivosEmAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado registrado no ano de " + ano + ".");
            } else {
                System.out.println("----- AUTORES VIVOS EM " + ano + " -----");
                autoresVivos.forEach(System.out::println);
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um ano válido (número).");
            leitura.nextLine(); // Limpa o buffer
        }
    }

    /**
     * Lista os livros registrados no banco de dados com base no idioma escolhido pelo usuário.
     */
    private void listarLivrosPorIdioma() {
        System.out.println("""
            Digite o código do idioma para busca:
            es - espanhol
            en - inglês
            fr - francês
            pt - português
            """);
        var idiomaEscolhido = leitura.nextLine();

        List<Book> livrosPorIdioma = bookRepository.findByLanguage(idiomaEscolhido);

        if (livrosPorIdioma.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o idioma '" + idiomaEscolhido + "'.");
        } else {
            System.out.println("----- LIVROS NO IDIOMA '" + idiomaEscolhido + "' -----");
            livrosPorIdioma.forEach(System.out::println);
        }
    }

    /**
     * Busca os 10 livros mais populares na API Gutendex e os salva no banco de dados
     * se ainda não estiverem registrados.
     * @Transactional garante que todas as operações de banco de dados dentro deste método
     * sejam executadas como uma única transação.
     */
    @Transactional
    private void buscarTop10Livros() {
        System.out.println("Buscando os 10 livros mais populares na API...");
        try {
            var json = consumo.obterDados(ENDERECO_BASE);
            GutendexResponse dados = conversor.obterDados(json, GutendexResponse.class);

            System.out.println("\n----- TOP 10 LIVROS -----");
            dados.getResults().stream()
                    .limit(10)
                    .forEach(livro -> {
                        // Lógica para evitar duplicatas, igual à busca individual.
                        Optional<Book> livroExistente = bookRepository.findByTitleContainsIgnoreCase(livro.getTitle());
                        if (livroExistente.isEmpty()) {
                            Author autorDoLivro = livro.getAuthor();
                            if (autorDoLivro != null) {
                                Optional<Author> autorExistente = authorRepository.findByNameContainsIgnoreCase(autorDoLivro.getName());
                                livro.setAuthor(autorExistente.orElse(autorDoLivro));
                            }
                            bookRepository.save(livro);
                            System.out.println("Livro salvo: " + livro.getTitle());
                        }
                    });
            System.out.println("\nOs 10 livros mais populares foram verificados e salvos (se novos) no banco de dados.");

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao buscar o top 10: " + e.getMessage());
        }
    }

    /**
     * Calcula e exibe estatísticas gerais sobre os livros registrados no banco de dados,
     * como total, média, mínimo e máximo de downloads.
     */
    private void exibirEstatisticasGerais() {
        List<Book> livros = bookRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro no banco para gerar estatísticas.");
            return;
        }

        // Usa DoubleSummaryStatistics para calcular de forma eficiente várias estatísticas de uma vez.
        java.util.DoubleSummaryStatistics stats = livros.stream()
                .filter(livro -> livro.getDownloadCount() != null) // Garante que não haja NullPointerException
                .mapToDouble(Book::getDownloadCount)
                .summaryStatistics();

        System.out.println("\n----- ESTATÍSTICAS GERAIS -----");
        System.out.println("Total de livros registrados: " + stats.getCount());
        System.out.println("Livro com mais downloads: " + (long)stats.getMax());
        System.out.println("Livro com menos downloads: " + (long)stats.getMin());
        System.out.println("Média de downloads: " + String.format("%.2f", stats.getAverage()));
        System.out.println("--------------------------------\n");
    }
}