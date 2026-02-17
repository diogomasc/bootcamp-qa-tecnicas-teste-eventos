package com.bootcamp.eventos.dto;

/**
 * Record representando o resultado de uma validação de convite.
 * 
 * @param valido       true se o convite passou em todas as validações
 * @param mensagemErro Mensagem descrevendo o erro, ou null se válido
 */
public record ResultadoValidacao(boolean valido, String mensagemErro) {

  public boolean isValido() {
    return valido;
  }

  public String getMensagemErro() {
    return mensagemErro;
  }
}
