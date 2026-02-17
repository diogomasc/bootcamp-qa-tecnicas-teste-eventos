package com.bootcamp.eventos.boundary;

import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dominio.Participante;
import com.bootcamp.eventos.exception.EventoLotadoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Análise de Valor Limite (Boundary Testing) para a classe Evento.
 * 
 * TÉCNICA: Boundary Testing
 * FOCO: Testar limites de participantes e validação de data
 * ESTRATÉGIA: on-point, off-point, in-point
 */
class EventoBoundaryTest {

  // ==================== BOUNDARY: Número de Participantes ====================

  @Test
  void deveAceitarConfirmacaoQuandoAbaixoDoLimite() {
    // Arrange - in-point: bem dentro do limite
    Evento evento = new Evento("Festa de Casamento", 100, LocalDateTime.now().plusDays(30));
    preencherParticipantes(evento, 98); // 98 confirmados

    // Act
    boolean resultado = evento.confirmarParticipante(
        new Participante("João Silva", "Levarei sobremesa"));

    // Assert
    assertThat(resultado).isTrue();
    assertThat(evento.getNumeroConfirmados()).isEqualTo(99); // on-point: 99 (quase no limite)
  }

  @Test
  void deveAceitarConfirmacaoNoLimiteExato() {
    // Arrange
    Evento evento = new Evento("Festa de Casamento", 100, LocalDateTime.now().plusDays(30));
    preencherParticipantes(evento, 99); // 99 confirmados

    // Act
    boolean resultado = evento.confirmarParticipante(
        new Participante("Maria Santos", null));

    // Assert - on-point: exatamente no limite de 100
    assertThat(resultado).isTrue();
    assertThat(evento.getNumeroConfirmados()).isEqualTo(100);
  }

  @Test
  void deveRecusarConfirmacaoAcimaDoLimite() {
    // Arrange
    Evento evento = new Evento("Festa de Casamento", 100, LocalDateTime.now().plusDays(30));
    preencherParticipantes(evento, 100); // já lotado

    // Act & Assert - off-point: tentativa de adicionar 101º participante
    assertThatThrownBy(() -> evento.confirmarParticipante(new Participante("Pedro Costa", null)))
        .isInstanceOf(EventoLotadoException.class)
        .hasMessage("Evento já atingiu o número máximo de participantes");
  }

  @Test
  void deveAceitarConfirmacaoQuandoEventoIlimitado() {
    // Arrange - evento sem limite
    Evento evento = new Evento("Corrida Solidária", null, LocalDateTime.now().plusDays(40));
    preencherParticipantes(evento, 200);

    // Act
    boolean resultado = evento.confirmarParticipante(
        new Participante("Ana Paula", "Confirmo presença!"));

    // Assert - sem limite, sempre aceita
    assertThat(resultado).isTrue();
    assertThat(evento.getNumeroConfirmados()).isEqualTo(201);
  }

  // ==================== BOUNDARY: Validação de Data ====================

  @Test
  void deveRejeitarEventoComDataNoPassado() {
    // Arrange - off-point: data no passado
    LocalDateTime dataPassada = LocalDateTime.now().minusDays(1);

    // Act & Assert
    assertThatThrownBy(() -> new Evento("Reunião", 50, dataPassada))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("A data do evento não pode ser no passado");
  }

  @Test
  void deveAceitarEventoComDataHoje() {
    // Arrange - on-point: limite inferior (hoje)
    LocalDateTime hoje = LocalDateTime.now();

    // Act
    Evento evento = new Evento("Workshop", 30, hoje);

    // Assert
    assertThat(evento.getData()).isEqualTo(hoje);
  }

  @Test
  void deveAceitarEventoComDataFutura() {
    // Arrange - on-point: logo após o limite
    LocalDateTime amanha = LocalDateTime.now().plusDays(1);

    // Act
    Evento evento = new Evento("Palestra", 100, amanha);

    // Assert
    assertThat(evento.getData()).isEqualTo(amanha);
  }

  @Test
  void deveAceitarEventoComDataMuitoFutura() {
    // Arrange - in-point: bem dentro da partição válida
    LocalDateTime umAnoDepois = LocalDateTime.now().plusYears(1);

    // Act
    Evento evento = new Evento("Conferência Anual", 200, umAnoDepois);

    // Assert
    assertThat(evento.getData()).isEqualTo(umAnoDepois);
  }

  // ==================== Métodos Auxiliares ====================

  private void preencherParticipantes(Evento evento, int quantidade) {
    for (int i = 1; i <= quantidade; i++) {
      evento.confirmarParticipante(
          new Participante("Participante " + i, null));
    }
  }
}
