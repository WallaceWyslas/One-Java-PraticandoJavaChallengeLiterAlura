package br.com.literalura.literalura.client;

import br.com.literalura.literalura.model.GutendexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GutendexClient {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://gutendex.com/books?search=tolstoy"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            GutendexResponse gutendexResponse = mapper.readValue(response.body(), GutendexResponse.class);

            System.out.println("Total de livros: " + gutendexResponse.getCount());
            gutendexResponse.getResults().forEach(livro -> {
                System.out.println("Título: " +livro.getTitle());
                livro.getAuthors().forEach(autor -> System.out.println("Autor: " + autor.getName()));
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
