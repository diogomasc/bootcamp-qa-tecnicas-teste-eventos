package com.bootcamp.eventos.dominio;

/**
 * Record representando um participante de evento.
 * Imutável e contém informações básicas do participante.
 * 
 * @param nome       Nome do participante (obrigatório)
 * @param observacao Observação opcional do participante (ex: "Levarei
 *                   sobremesa")
 */
public record Participante(String nome, String observacao) {
  public Participante {
    if (nome == null || nome.isBlank()) {
      throw new IllegalArgumentException("Nome do participante não pode ser vazio");
    }
  }
}
