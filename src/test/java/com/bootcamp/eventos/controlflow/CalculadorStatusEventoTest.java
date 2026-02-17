package com.bootcamp.eventos.controlflow;

import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dominio.Participante;
import com.bootcamp.eventos.dominio.enums.StatusPercentual;
import com.bootcamp.eventos.servico.CalculadorStatusEvento;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Fluxo de Controle para CalculadorStatusEvento.
 * 
 * TÉCNICA: Control Flow Testing (White Box)
 * FOCO: 100% branch coverage
 * OBJETIVO: Cobrir todos os caminhos de decisão
 */
class CalculadorStatusEventoTest {

  @Test
  void deveRetornarSemLimiteQuandoEventoNaoTemLimite() {
    // Branch 1 (true): !evento.temLimite() = true
    Evento evento = criarEventoSemLimite();
    preencherParticipantes(evento, 200);
    CalculadorStatusEvento calculador = new CalculadorStatusEvento();

    StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

    assertThat(status).isEqualTo(StatusPercentual.SEM_LIMITE);
  }

  @Test
  void deveRetornarLotadoQuando100PorCento() {
    // Branch 1 (false): evento tem limite
    // Branch 2 (true): percentual >= 100
    Evento evento = criarEvento(100);
    preencherParticipantes(evento, 100);
    CalculadorStatusEvento calculador = new CalculadorStatusEvento();

    StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

    assertThat(status).isEqualTo(StatusPercentual.LOTADO);
  }

  @Test
  void deveRetornarQuaseLotadoQuando80A99PorCento() {
    // Branch 1 (false): evento tem limite
    // Branch 2 (false): percentual < 100
    // Branch 3 (true): percentual >= 80
    Evento evento = criarEvento(100);
    preencherParticipantes(evento, 85);
    CalculadorStatusEvento calculador = new CalculadorStatusEvento();

    StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

    assertThat(status).isEqualTo(StatusPercentual.QUASE_LOTADO);
  }

  @Test
  void deveRetornarBoaAdesaoQuando50A79PorCento() {
    // Branch 1 (false): evento tem limite
    // Branch 2 (false): percentual < 100
    // Branch 3 (false): percentual < 80
    // Branch 4 (true): percentual >= 50
    Evento evento = criarEvento(100);
    preencherParticipantes(evento, 60);
    CalculadorStatusEvento calculador = new CalculadorStatusEvento();

    StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

    assertThat(status).isEqualTo(StatusPercentual.BOA_ADESAO);
  }

  @Test
  void deveRetornarAbertoComConfirmacoesQuando1A49PorCento() {
    // Branch 1 (false): evento tem limite
    // Branch 2 (false): percentual < 100
    // Branch 3 (false): percentual < 80
    // Branch 4 (false): percentual < 50
    // Branch 5 (true): percentual > 0
    Evento evento = criarEvento(100);
    preencherParticipantes(evento, 20);
    CalculadorStatusEvento calculador = new CalculadorStatusEvento();

    StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

    assertThat(status).isEqualTo(StatusPercentual.ABERTO_COM_CONFIRMACOES);
  }

  @Test
  void deveRetornarAbertoSemConfirmacoesQuandoZeroPorCento() {
    // Branch 1 (false): evento tem limite
    // Branch 2 (false): percentual < 100
    // Branch 3 (false): percentual < 80
    // Branch 4 (false): percentual < 50
    // Branch 5 (false): percentual = 0
    Evento evento = criarEvento(100);
    CalculadorStatusEvento calculador = new CalculadorStatusEvento();

    StatusPercentual status = calculador.calcularStatusPercentualConfirmados(evento);

    assertThat(status).isEqualTo(StatusPercentual.ABERTO_SEM_CONFIRMACOES);
  }

  // ==================== Métodos Auxiliares ====================

  private Evento criarEvento(int limite) {
    return new Evento("Evento Teste", limite, LocalDateTime.now().plusDays(30));
  }

  private Evento criarEventoSemLimite() {
    return new Evento("Evento Sem Limite", null, LocalDateTime.now().plusDays(30));
  }

  private void preencherParticipantes(Evento evento, int quantidade) {
    for (int i = 1; i <= quantidade; i++) {
      evento.confirmarParticipante(new Participante("Participante " + i, null));
    }
  }
}
