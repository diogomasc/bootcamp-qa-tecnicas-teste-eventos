package com.bootcamp.eventos.dominio;

/**
 * Interface selada representando um usuário do sistema.
 * Implementada por {@link Organizador} e outros tipos de usuários.
 * Sealed garante que apenas os tipos permitidos implementem esta interface.
 */
public sealed interface Usuario permits Organizador, UsuarioComum {

  String id();

  String nome();

  String email();

  boolean isOrganizador();
}
