package com.caridade.exception.solicitacao;

public class SolicitacaoOperacaoInvalidaException extends RuntimeException {

    public SolicitacaoOperacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}