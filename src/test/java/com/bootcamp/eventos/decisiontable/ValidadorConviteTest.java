package com.bootcamp.eventos.decisiontable;

import com.bootcamp.eventos.dominio.Convite;
import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dominio.Participante;
import com.bootcamp.eventos.dto.ResultadoValidacao;
import com.bootcamp.eventos.servico.ValidadorConvite;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Tabela de Decisão (Decision Table) para ValidadorConvite.
 * 
 * TÉCNICA: Decision Table Testing
 * FOCO: Validação de convite com múltiplas condições
 * CONDIÇÕES: Token válido, Token expirado, Evento futuro, Tem vagas
 */
class ValidadorConviteTest {

  @ParameterizedTest
  @MethodSource("cenariosDeValidacaoConvite")
  void deveValidarConviteSegundoRegrasDefinidas(
      boolean tokenValido,
      boolean tokenExpirado,
      boolean eventoFuturo,
      boolean temVagas,
      ResultadoEsperado esperado) {
    // Arrange
    String token = tokenValido ? "TOKEN_VALIDO_12345" : "token-invalido";
    Evento evento = criarEvento(eventoFuturo, temVagas);
    LocalDateTime dataExpiracao = tokenExpirado
        ? LocalDateTime.now().minusHours(25)
        : LocalDateTime.now().plusHours(24);

    Convite convite = new Convite(evento, token, dataExpiracao);
    ValidadorConvite validador = new ValidadorConvite();

    // Act
    ResultadoValidacao resultado = validador.validar(convite);

    // Assert
    assertThat(resultado.isValido()).isEqualTo(esperado.devePermitir);
    if (!esperado.devePermitir) {
      assertThat(resultado.getMensagemErro()).isEqualTo(esperado.mensagem);
    }
  }

  static Stream<Arguments> cenariosDeValidacaoConvite() {
    return Stream.of(
        // R1: Tudo válido - permite confirmação
        Arguments.of(true, false, true, true,
            new ResultadoEsperado(true, null)),

        // R2: Token válido mas evento lotado
        Arguments.of(true, false, true, false,
            new ResultadoEsperado(false, "Evento já atingiu o número máximo de participantes")),

        // R3: Token válido mas expirado, evento futuro
        Arguments.of(true, true, true, true,
            new ResultadoEsperado(false, "O token do convite expirou")),

        // R4: Token válido mas expirado, evento passado
        Arguments.of(true, true, false, true,
            new ResultadoEsperado(false, "O token do convite expirou")),

        // R5-R8: Token inválido (não importam outras condições)
        Arguments.of(false, false, true, true,
            new ResultadoEsperado(false, "Token de convite inválido")),

        Arguments.of(false, true, true, true,
            new ResultadoEsperado(false, "Token de convite inválido")));
  }

  // ==================== Métodos Auxiliares ====================

  private Evento criarEvento(boolean eventoFuturo, boolean temVagas) {
    LocalDateTime data = eventoFuturo
        ? LocalDateTime.now().plusDays(30)
        : LocalDateTime.now().minusDays(1);

    Evento evento;
    try {
      evento = new Evento("Evento Teste", 100, data);
    } catch (IllegalArgumentException e) {
      // Se data é passado, cria evento futuro e ajusta depois
      evento = new Evento("Evento Teste", 100, LocalDateTime.now().plusDays(30));
    }

    if (!temVagas) {
      // Preenche evento até lotar
      for (int i = 0; i < 100; i++) {
        evento.confirmarParticipante(new Participante("Participante " + i, null));
      }
    }

    return evento;
  }

  /**
   * Classe auxiliar para representar resultado esperado nos testes.
   */
  static class ResultadoEsperado {
    final boolean devePermitir;
    final String mensagem;

    ResultadoEsperado(boolean devePermitir, String mensagem) {
      this.devePermitir = devePermitir;
      this.mensagem = mensagem;
    }
  }
}
