package com.bootcamp.eventos.servico.email;

import com.bootcamp.eventos.dominio.Convite;
import com.bootcamp.eventos.dominio.Evento;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementação fake do ServicoEmail para testes.
 * Armazena emails enviados em memória e permite verificação.
 */
public class ServicoEmailFake implements ServicoEmail {

  private final List<EmailEnviado> emailsEnviados = new ArrayList<>();

  @Override
  public void enviarConvite(String destinatario, Convite convite) {
    String mensagem = String.format(
        "[CONVITE] Você foi convidado para o evento: %s",
        convite.getEvento().getNome());

    registrarEmail(destinatario, "Convite para Evento", mensagem);
    System.out.println("✉️  Email enviado para: " + destinatario);
    System.out.println("    " + mensagem);
  }

  @Override
  public void enviarConfirmacao(String destinatario, Evento evento) {
    String mensagem = String.format(
        "[CONFIRMAÇÃO] Sua presença no evento '%s' foi confirmada!",
        evento.getNome());

    registrarEmail(destinatario, "Confirmação de Presença", mensagem);
    System.out.println("✉️  Email enviado para: " + destinatario);
    System.out.println("    " + mensagem);
  }

  @Override
  public void enviarNotificacaoAlteracao(String destinatario, Evento evento) {
    String mensagem = String.format(
        "[ALTERAÇÃO] O evento '%s' foi alterado. Verifique os detalhes.",
        evento.getNome());

    registrarEmail(destinatario, "Alteração de Evento", mensagem);
    System.out.println("✉️  Email enviado para: " + destinatario);
    System.out.println("    " + mensagem);
  }

  /**
   * Retorna a quantidade de emails enviados.
   */
  public int getQuantidadeEmailsEnviados() {
    return emailsEnviados.size();
  }

  /**
   * Retorna todos os emails enviados.
   */
  public List<EmailEnviado> getEmailsEnviados() {
    return new ArrayList<>(emailsEnviados);
  }

  /**
   * Limpa a lista de emails enviados (útil entre testes).
   */
  public void limpar() {
    emailsEnviados.clear();
  }

  private void registrarEmail(String destinatario, String assunto, String mensagem) {
    emailsEnviados.add(new EmailEnviado(destinatario, assunto, mensagem));
  }

  /**
   * Record representando um email enviado.
   */
  public record EmailEnviado(String destinatario, String assunto, String mensagem) {
  }
}
