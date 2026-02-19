package com.bootcamp.eventos.servico;

import com.bootcamp.eventos.dominio.Convite;
import com.bootcamp.eventos.dominio.Evento;
import com.bootcamp.eventos.dominio.Participante;
import com.bootcamp.eventos.dominio.RespostaConvite;
import com.bootcamp.eventos.dto.ResultadoProcessamento;

/**
 * Processador de respostas a convites.
 * Implementado com foco em 100% branch coverage.
 */
public class ProcessadorResposta {

  public ResultadoProcessamento processarResposta(
      Convite convite,
      RespostaConvite resposta) {
    // Validação de entrada
    if (convite == null || resposta == null) {
      throw new IllegalArgumentException("Convite e resposta não podem ser nulos");
    }

    // Verifica se convite já foi respondido
    if (convite.foiRespondido()) {
      return new ResultadoProcessamento(false, "Convite já foi respondido anteriormente");
    }

    // Processa aceitação
    if (resposta.aceitou()) {
      Evento evento = convite.getEvento();

      // Verifica se ainda há vagas
      if (evento.temLimite() && evento.estaLotado()) {
        convite.marcarComoRecusado("Evento lotado");
        return new ResultadoProcessamento(false, "Evento já está lotado");
      }

      // Confirma participação
      Participante participante = new Participante(
          resposta.nome(),
          resposta.getObservacao());

      evento.adicionarParticipante(participante);
      convite.marcarComoAceito(participante);

      return new ResultadoProcessamento(true, "Participação confirmada com sucesso");
    }
    // Processa recusa
    else {
      convite.marcarComoRecusado(resposta.getMotivoRecusa());
      return new ResultadoProcessamento(true, "Recusa registrada");
    }
  }
}
