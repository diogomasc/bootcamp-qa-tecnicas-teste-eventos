package com.bootcamp.eventos.dominio.enums;

/**
 * Enum representando os diferentes status percentuais de confirmação de um
 * evento.
 * Usado para classificar eventos baseado na proporção de vagas ocupadas.
 */
public enum StatusPercentual {
  /** Evento sem limite de participantes */
  SEM_LIMITE,

  /** Evento com 100% das vagas ocupadas */
  LOTADO,

  /** Evento com 80-99% das vagas ocupadas */
  QUASE_LOTADO,

  /** Evento com 50-79% das vagas ocupadas */
  BOA_ADESAO,

  /** Evento com 1-49% das vagas ocupadas */
  ABERTO_COM_CONFIRMACOES,

  /** Evento sem nenhuma confirmação (0% ocupado) */
  ABERTO_SEM_CONFIRMACOES
}
