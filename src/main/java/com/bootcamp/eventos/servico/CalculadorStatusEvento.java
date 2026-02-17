package com.bootcamp.eventos.servico;

import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dominio.enums.StatusPercentual;

/**
 * Calculador de status percentual de eventos.
 * Implementado com foco em branch coverage completo.
 */
public class CalculadorStatusEvento {

  public StatusPercentual calcularStatusPercentualConfirmados(Evento evento) {
    // Evento sem limite sempre retorna status especial
    if (!evento.temLimite()) {
      return StatusPercentual.SEM_LIMITE;
    }

    int percentualConfirmados = evento.calcularPercentualConfirmados();

    if (percentualConfirmados >= 100) {
      return StatusPercentual.LOTADO;
    } else if (percentualConfirmados >= 80) {
      return StatusPercentual.QUASE_LOTADO;
    } else if (percentualConfirmados >= 50) {
      return StatusPercentual.BOA_ADESAO;
    } else if (percentualConfirmados > 0) {
      return StatusPercentual.ABERTO_COM_CONFIRMACOES;
    } else {
      return StatusPercentual.ABERTO_SEM_CONFIRMACOES;
    }
  }
}
