package br.com.literalura.literalura;

import br.com.literalura.literalura.repository.AuthorRepository;
import br.com.literalura.literalura.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada principal da aplicação Spring Boot.
 * A implementação da interface CommandLineRunner permite executar código
 * após a inicialização completa do contexto da aplicação.
 */
@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	// A anotação @Autowired realiza a injeção de dependência automática dos repositórios.
	// O Spring encontra uma implementação para BookRepository e AuthorRepository e a fornece aqui.
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;

	/**
	 * Método principal padrão do Java, que inicia a aplicação Spring.
	 */
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	/**
	 * Este método é executado automaticamente pelo Spring Boot após a aplicação iniciar.
	 * É o local ideal para iniciar a lógica da nossa aplicação de console.
	 *
	 * @param args Argumentos de linha de comando (não utilizados neste projeto).
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		// Cria uma instância da nossa classe principal, passando os repositórios injetados.
		Principal principal = new Principal(bookRepository, authorRepository);
		// Inicia o menu interativo com o usuário.
		principal.exibeMenu();
	}
}