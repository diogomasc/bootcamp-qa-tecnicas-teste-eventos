package com.bootcamp.eventos.dto;

import com.bootcamp.eventos.dominio.enums.TipoPermissao;

/**
 * Record representando permissão de edição de um evento.
 * 
 * @param tipo          Tipo da permissão (COMPLETA, LIMITADA, BLOQUEADA,
 *                      NEGADA)
 * @param justificativa Justificativa da permissão concedida ou negada
 */
public record PermissaoEdicao(TipoPermissao tipo, String justificativa) {
}
