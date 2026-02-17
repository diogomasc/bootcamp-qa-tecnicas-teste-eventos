package com.bootcamp.eventos.servico.email;

import com.bootcamp.eventos.dominio.Convite;
import com.bootcamp.eventos.dominio.Evento;

/**
 * Interface para serviço de envio de emails.
 * Será implementada por versões fake (testes) ou reais (produção).
 */
public interface ServicoEmail {

  /**
   * Envia um convite por email.
   */
  void enviarConvite(String destinatario, Convite convite);

  /**
   * Envia confirmação de participação por email.
   */
  void enviarConfirmacao(String destinatario, Evento evento);

  /**
   * Envia notificação de alteração do evento por email.
   */
  void enviarNotificacaoAlteracao(String destinatario, Evento evento);
}
