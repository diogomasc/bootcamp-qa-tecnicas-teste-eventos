package com.bootcamp.eventos.servico;

import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dominio.Usuario;
import com.bootcamp.eventos.dominio.enums.TipoPermissao;
import com.bootcamp.eventos.dto.PermissaoEdicao;

import java.time.LocalDateTime;

/**
 * Serviço de gerenciamento de eventos.
 * Gerencia operações e permissões relacionadas a eventos.
 */
public class ServicoEvento {

  /**
   * Verifica a permissão de edição de um usuário para um evento.
   * 
   * Decision Table:
   * - É organizador?
   * - Evento futuro?
   * - Tem confirmados?
   */
  public PermissaoEdicao verificarPermissaoEdicao(Usuario usuario, Evento evento) {
    // Regra: Não organizador não pode editar
    if (!usuario.isOrganizador()) {
      return new PermissaoEdicao(
          TipoPermissao.NEGADA,
          "Apenas organizadores podem editar eventos");
    }

    // Regra: Evento passado não pode ser editado
    boolean eventoFuturo = evento.getData().isAfter(LocalDateTime.now());
    if (!eventoFuturo) {
      return new PermissaoEdicao(
          TipoPermissao.BLOQUEADA,
          "Eventos passados não podem ser editados");
    }

    // Regra: Evento com confirmados tem edição limitada
    boolean temConfirmados = evento.getNumeroConfirmados() > 0;
    if (temConfirmados) {
      return new PermissaoEdicao(
          TipoPermissao.LIMITADA,
          "Evento com confirmados permite apenas edições limitadas");
    }

    // Edição completa: organizador, evento futuro, sem confirmados
    return new PermissaoEdicao(
        TipoPermissao.COMPLETA,
        "Edição completa permitida");
  }
}
