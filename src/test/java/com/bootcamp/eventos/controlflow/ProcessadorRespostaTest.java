package com.bootcamp.eventos.controlflow;

import com.bootcamp.eventos.dominio.Convite;
import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dominio.Participante;
import com.bootcamp.eventos.dominio.RespostaConvite;
import com.bootcamp.eventos.dto.ResultadoProcessamento;
import com.bootcamp.eventos.servico.ProcessadorResposta;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Fluxo de Controle / Caixa Branca para ProcessadorResposta.
 * 
 * TÉCNICA: Control Flow Testing (White Box)
 * FOCO: 100% branch coverage
 * OBJETIVO: Exercitar todos os caminhos de execução do código
 */
class ProcessadorRespostaTest {

  @Test
  void deveLancarExcecaoQuandoConviteNulo() {
    // Branch 1 (true): convite == null
    ProcessadorResposta processador = new ProcessadorResposta();
    RespostaConvite resposta = new RespostaConvite(true, "João", null);

    assertThatThrownBy(() -> processador.processarResposta(null, resposta))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Convite e resposta não podem ser nulos");
  }

  @Test
  void deveLancarExcecaoQuandoRespostaNula() {
    // Branch 1 (true): resposta == null
    ProcessadorResposta processador = new ProcessadorResposta();
    Convite convite = criarConviteValido();

    assertThatThrownBy(() -> processador.processarResposta(convite, null))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void deveRejeitarQuandoConviteJaRespondido() {
    // Branch 1 (false): entradas válidas
    // Branch 2 (true): convite.foiRespondido() = true
    ProcessadorResposta processador = new ProcessadorResposta();
    Convite convite = criarConviteJaRespondido();
    RespostaConvite resposta = new RespostaConvite(true, "Maria", null);

    ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

    assertThat(resultado.sucesso()).isFalse();
    assertThat(resultado.mensagem()).isEqualTo("Convite já foi respondido anteriormente");
  }

  @Test
  void deveAceitarParticipacaoQuandoEventoTemVagas() {
    // Branch 1 (false): entradas válidas
    // Branch 2 (false): convite não foi respondido
    // Branch 3 (true): resposta aceita
    // Branch 4 (false): evento não está lotado
    ProcessadorResposta processador = new ProcessadorResposta();
    Convite convite = criarConviteNaoRespondido();
    RespostaConvite resposta = new RespostaConvite(true, "Carlos", "Levarei refrigerante");

    ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

    assertThat(resultado.sucesso()).isTrue();
    assertThat(resultado.mensagem()).isEqualTo("Participação confirmada com sucesso");
    assertThat(convite.foiRespondido()).isTrue();
    assertThat(convite.foiAceito()).isTrue();
  }

  @Test
  void deveRecusarQuandoEventoLotado() {
    // Branch 1 (false): entradas válidas
    // Branch 2 (false): convite não foi respondido
    // Branch 3 (true): resposta aceita
    // Branch 4 (true): evento.temLimite() && evento.estaLotado()
    ProcessadorResposta processador = new ProcessadorResposta();
    Convite convite = criarConviteParaEventoLotado();
    RespostaConvite resposta = new RespostaConvite(true, "Ana", null);

    ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

    assertThat(resultado.sucesso()).isFalse();
    assertThat(resultado.mensagem()).isEqualTo("Evento já está lotado");
    assertThat(convite.foiRecusado()).isTrue();
  }

  @Test
  void deveRegistrarRecusaDoParticipante() {
    // Branch 1 (false): entradas válidas
    // Branch 2 (false): convite não foi respondido
    // Branch 3 (false): resposta recusada
    ProcessadorResposta processador = new ProcessadorResposta();
    Convite convite = criarConviteNaoRespondido();
    RespostaConvite resposta = new RespostaConvite(false, null, "Tenho outro compromisso");

    ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

    assertThat(resultado.sucesso()).isTrue();
    assertThat(resultado.mensagem()).isEqualTo("Recusa registrada");
    assertThat(convite.foiRecusado()).isTrue();
  }

  @Test
  void deveAceitarParticipacaoQuandoEventoSemLimite() {
    // Branch 1 (false): entradas válidas
    // Branch 2 (false): convite não foi respondido
    // Branch 3 (true): resposta aceita
    // Branch 4 (false): evento sem limite (temLimite() = false)
    ProcessadorResposta processador = new ProcessadorResposta();
    Convite convite = criarConviteParaEventoSemLimite();
    RespostaConvite resposta = new RespostaConvite(true, "Roberto", null);

    ResultadoProcessamento resultado = processador.processarResposta(convite, resposta);

    assertThat(resultado.sucesso()).isTrue();
    assertThat(convite.foiAceito()).isTrue();
  }

  // ==================== Métodos Auxiliares ====================

  private Convite criarConviteValido() {
    Evento evento = new Evento("Workshop", 50, LocalDateTime.now().plusDays(30));
    return new Convite(evento, "TOKEN_VALIDO", LocalDateTime.now().plusHours(24));
  }

  private Convite criarConviteNaoRespondido() {
    Evento evento = new Evento("Festa", 100, LocalDateTime.now().plusDays(30));
    return new Convite(evento, "TOKEN_VALIDO", LocalDateTime.now().plusHours(24));
  }

  private Convite criarConviteJaRespondido() {
    Convite convite = criarConviteNaoRespondido();
    convite.marcarComoAceito(new Participante("Alguém", null));
    return convite;
  }

  private Convite criarConviteParaEventoLotado() {
    Evento evento = new Evento("Palestra", 10, LocalDateTime.now().plusDays(30));
    // Lotar o evento
    for (int i = 0; i < 10; i++) {
      evento.confirmarParticipante(new Participante("Participante " + i, null));
    }
    return new Convite(evento, "TOKEN_VALIDO", LocalDateTime.now().plusHours(24));
  }

  private Convite criarConviteParaEventoSemLimite() {
    Evento evento = new Evento("Corrida", null, LocalDateTime.now().plusDays(30));
    return new Convite(evento, "TOKEN_VALIDO", LocalDateTime.now().plusHours(24));
  }
}
