package br.com.literalura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementação da interface IConverteDados que utiliza a biblioteca Jackson
 * para realizar a conversão de JSON para objetos Java.
 */
public class ConverteDados implements IConverteDados {
    // ObjectMapper é a classe principal do Jackson para ler e escrever JSON.
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converte uma String JSON em um objeto do tipo especificado.
     *
     * @param json A String JSON a ser convertida.
     * @param classe O tipo (Class) do objeto de destino.
     * @return Uma instância do objeto de destino populada com os dados do JSON.
     * @throws RuntimeException se ocorrer um erro durante o processamento do JSON.
     */
    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}