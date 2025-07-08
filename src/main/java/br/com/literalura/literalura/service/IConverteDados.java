package br.com.literalura.literalura.service;

/**
 * Interface que define um contrato para serviços de conversão de dados.
 * O uso de uma interface permite desacoplar a implementação (ex: Jackson) do resto do código.
 */
public interface IConverteDados {
    /**
     * Método genérico para obter dados convertidos.
     * @param json A String com os dados (ex: JSON).
     * @param classe A classe de destino para a conversão.
     * @param <T> O tipo genérico da classe de destino.
     * @return Uma instância de T.
     */
    <T> T obterDados(String json, Class<T> classe);
}