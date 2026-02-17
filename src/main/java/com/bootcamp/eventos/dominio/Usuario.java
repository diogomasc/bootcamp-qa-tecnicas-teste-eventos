package com.bootcamp.eventos.dominio;

/**
 * Classe abstrata representando um usuário do sistema.
 * Pode ser estendida por Organizador ou outros tipos de usuários.
 */
public abstract class Usuario {
  protected final String id;
  protected final String nome;
  protected final String email;

  public Usuario(String id, String nome, String email) {
    this.id = id;
    this.nome = nome;
    this.email = email;
  }

  public abstract boolean isOrganizador();

  public String getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }
}
