package com.bootcamp.eventos.dominio;

/**
 * Record representando uma resposta a um convite.
 * Pode ser uma aceitação (com nome e observação) ou recusa (com motivo).
 * 
 * @param aceitou    Se true, é uma aceitação; se false, é uma recusa
 * @param nome       Nome do participante (obrigatório se aceitou = true)
 * @param observacao Observação do participante ao aceitar ou motivo da recusa
 */
public record RespostaConvite(boolean aceitou, String nome, String observacao) {

  public String getMotivoRecusa() {
    return !aceitou ? observacao : null;
  }

  public String getObservacao() {
    return aceitou ? observacao : null;
  }

  public boolean isAceitou() {
    return aceitou;
  }

  public String getNome() {
    return nome;
  }
}
