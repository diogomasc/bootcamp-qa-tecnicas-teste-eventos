package com.bootcamp.eventos.dominio;

/**
 * Record representando um usuário comum do sistema.
 * Usuários comuns não possuem permissões de organização de eventos.
 *
 * @param id    Identificador único do usuário
 * @param nome  Nome do usuário
 * @param email Email do usuário
 */
public record UsuarioComum(String id, String nome, String email) implements Usuario {

  @Override
  public boolean isOrganizador() {
    return false;
  }
}
