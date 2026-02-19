package com.bootcamp.eventos.dominio;

/**
 * Record representando um organizador de eventos.
 * Organizadores têm permissões especiais para criar e editar eventos.
 * Implementa {@link Usuario} como tipo selado.
 *
 * @param id    Identificador único do organizador
 * @param nome  Nome do organizador
 * @param email Email do organizador
 */
public record Organizador(String id, String nome, String email) implements Usuario {

  @Override
  public boolean isOrganizador() {
    return true;
  }
}
