package com.bootcamp.eventos.dominio;

import com.bootcamp.eventos.exception.EventoLotadoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal representando um evento.
 * Gerencia participantes, validações de data e limites.
 */
public class Evento {
  private final String nome;
  private final Integer limiteParticipantes; // null = sem limite
  private final LocalDateTime data;
  private final List<Participante> participantes;

  public Evento(String nome, Integer limiteParticipantes, LocalDateTime data) {
    validarData(data);
    this.nome = nome;
    this.limiteParticipantes = limiteParticipantes;
    this.data = data;
    this.participantes = new ArrayList<>();
  }

  /**
   * Confirma a participação de um participante no evento.
   * 
   * @param participante Participante a ser confirmado
   * @return true se confirmado com sucesso
   * @throws EventoLotadoException se evento já atingiu o limite
   */
  public boolean confirmarParticipante(Participante participante) {
    if (temLimite() && estaLotado()) {
      throw new EventoLotadoException("Evento já atingiu o número máximo de participantes");
    }

    participantes.add(participante);
    return true;
  }

  /**
   * Adiciona um participante diretamente (usado internamente).
   */
  public void adicionarParticipante(Participante participante) {
    confirmarParticipante(participante);
  }

  /**
   * Verifica se o evento tem limite de participantes.
   */
  public boolean temLimite() {
    return limiteParticipantes != null;
  }

  /**
   * Verifica se o evento está lotado (atingiu o limite).
   */
  public boolean estaLotado() {
    return temLimite() && participantes.size() >= limiteParticipantes;
  }

  /**
   * Retorna o número de participantes confirmados.
   */
  public int getNumeroConfirmados() {
    return participantes.size();
  }

  /**
   * Calcula o percentual de confirmados em relação ao limite.
   * 
   * @return Percentual de 0 a 100, ou 0 se não há limite
   */
  public int calcularPercentualConfirmados() {
    if (!temLimite()) {
      return 0;
    }

    return (participantes.size() * 100) / limiteParticipantes;
  }

  public LocalDateTime getData() {
    return data;
  }

  public String getNome() {
    return nome;
  }

  public Integer getLimiteParticipantes() {
    return limiteParticipantes;
  }

  private void validarData(LocalDateTime data) {
    if (data == null) {
      throw new IllegalArgumentException("A data do evento não pode ser nula");
    }

    LocalDateTime agora = LocalDateTime.now();
    // Aceita data igual ou futura
    if (data.isBefore(agora) && !data.withNano(0).equals(agora.withNano(0))) {
      throw new IllegalArgumentException("A data do evento não pode ser no passado");
    }
  }
}
