package com.bootcamp.eventos.exception;

/**
 * Exception lançada quando se tenta confirmar um participante em um evento
 * que já atingiu o número máximo de participantes.
 */
public class EventoLotadoException extends RuntimeException {
  public EventoLotadoException(String mensagem) {
    super(mensagem);
  }
}
