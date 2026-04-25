package com.caridade.exception.solicitacao;

import java.util.UUID;

public class SolicitacaoNaoEncontradaException extends RuntimeException {

    public SolicitacaoNaoEncontradaException(UUID id) {
        super("Solicitação com id " + id + " não encontrada");
    }
}