package com.bootcamp.eventos.dominio;

import java.time.LocalDateTime;

/**
 * Classe representando um convite para um evento.
 * Contém informações sobre o token, expiração e estado da resposta.
 */
public class Convite {
  private final Evento evento;
  private final String token;
  private final LocalDateTime dataExpiracao;
  private boolean respondido;
  private boolean aceito;
  private Participante participante;
  private String motivoRecusa;

  public Convite(Evento evento, String token, LocalDateTime dataExpiracao) {
    this.evento = evento;
    this.token = token;
    this.dataExpiracao = dataExpiracao;
    this.respondido = false;
    this.aceito = false;
  }

  public boolean foiRespondido() {
    return respondido;
  }

  public boolean foiAceito() {
    return respondido && aceito;
  }

  public boolean foiRecusado() {
    return respondido && !aceito;
  }

  public void marcarComoAceito(Participante participante) {
    this.respondido = true;
    this.aceito = true;
    this.participante = participante;
  }

  public void marcarComoRecusado(String motivo) {
    this.respondido = true;
    this.aceito = false;
    this.motivoRecusa = motivo;
  }

  public Evento getEvento() {
    return evento;
  }

  public String getToken() {
    return token;
  }

  public LocalDateTime getDataExpiracao() {
    return dataExpiracao;
  }

  public Participante getParticipante() {
    return participante;
  }

  public String getMotivoRecusa() {
    return motivoRecusa;
  }
}
