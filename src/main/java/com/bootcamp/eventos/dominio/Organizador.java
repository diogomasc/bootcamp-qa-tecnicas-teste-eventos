package com.bootcamp.eventos.dominio;

/**
 * Classe representando um organizador de eventos.
 * Organizadores têm permissões especiais para criar e editar eventos.
 */
public class Organizador extends Usuario {

  public Organizador(String id, String nome, String email) {
    super(id, nome, email);
  }

  @Override
  public boolean isOrganizador() {
    return true;
  }
}
