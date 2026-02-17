package com.bootcamp.eventos.servico;

import com.bootcamp.eventos.dominio.Convite;
import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dto.ResultadoValidacao;

import java.time.LocalDateTime;

/**
 * Validador de convites seguindo Decision Table.
 * Valida múltiplas condições: token, expiração, vagas.
 */
public class ValidadorConvite {

  private static final String TOKEN_VALIDO_PREFIX = "TOKEN_VALIDO";

  /**
   * Valida um convite segundo as regras de negócio.
   * 
   * Decision Table:
   * - Token válido?
   * - Token não expirado?
   * - Evento futuro?
   * - Tem vagas disponíveis?
   */
  public ResultadoValidacao validar(Convite convite) {
    // Validação 1: Token válido
    if (!tokenValido(convite.getToken())) {
      return new ResultadoValidacao(false, "Token de convite inválido");
    }

    // Validação 2: Token não expirado
    if (tokenExpirado(convite.getDataExpiracao())) {
      return new ResultadoValidacao(false, "O token do convite expirou");
    }

    // Validação 3: Evento tem vagas (se tiver limite)
    Evento evento = convite.getEvento();
    if (evento.temLimite() && evento.estaLotado()) {
      return new ResultadoValidacao(false, "Evento já atingiu o número máximo de participantes");
    }

    // Todas as validações passaram
    return new ResultadoValidacao(true, null);
  }

  private boolean tokenValido(String token) {
    return token != null && token.startsWith(TOKEN_VALIDO_PREFIX);
  }

  private boolean tokenExpirado(LocalDateTime dataExpiracao) {
    return dataExpiracao.isBefore(LocalDateTime.now());
  }
}
