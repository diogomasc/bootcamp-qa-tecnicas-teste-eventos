package com.bootcamp.eventos.dto;

/**
 * Record representando o resultado do processamento de uma resposta ao convite.
 * 
 * @param sucesso  true se a resposta foi processada com sucesso
 * @param mensagem Mensagem descrevendo o resultado
 */
public record ResultadoProcessamento(boolean sucesso, String mensagem) {
}
