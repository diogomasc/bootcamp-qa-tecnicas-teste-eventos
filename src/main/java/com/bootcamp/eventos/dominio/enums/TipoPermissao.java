package com.bootcamp.eventos.dominio.enums;

/**
 * Enum representando os diferentes tipos de permissão para edição de eventos.
 * Usado para controlar o que um usuário pode fazer com um evento.
 */
public enum TipoPermissao {
  /** Usuário pode editar todos os aspectos do evento */
  COMPLETA,

  /** Usuário pode editar apenas alguns aspectos (evento tem confirmados) */
  LIMITADA,

  /** Edição bloqueada porque evento já ocorreu */
  BLOQUEADA,

  /** Usuário não tem permissão (não é organizador) */
  NEGADA
}
