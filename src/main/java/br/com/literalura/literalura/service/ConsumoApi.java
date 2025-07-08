package br.com.literalura.literalura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Classe de serviço responsável por consumir uma API externa.
 * Utiliza o cliente HTTP moderno do Java (java.net.http).
 */
public class ConsumoApi {

    /**
     * Realiza uma requisição GET para um dado endereço e retorna o corpo da resposta como uma String JSON.
     *
     * @param endereco A URL completa da API a ser consultada.
     * @return Uma String contendo o JSON retornado pela API.
     * @throws RuntimeException se ocorrer um erro de I/O ou interrupção durante a chamada HTTP.
     */
    public String obterDados(String endereco) {
        // 1. Cria um cliente HTTP com configurações padrão.
        HttpClient client = HttpClient.newHttpClient();

        // 2. Constrói uma requisição HTTP para o URI fornecido.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        // 3. Envia a requisição e espera pela resposta.
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // Lança uma exceção de runtime para evitar o tratamento de exceções checadas em toda a aplicação.
            throw new RuntimeException(e);
        }

        // 4. Retorna o corpo da resposta.
        String json = response.body();
        return json;
    }
}