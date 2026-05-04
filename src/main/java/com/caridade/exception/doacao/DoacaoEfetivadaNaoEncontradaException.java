package com.caridade.exception.doacao;

import java.util.UUID;

public class DoacaoEfetivadaNaoEncontradaException extends RuntimeException {

    public DoacaoEfetivadaNaoEncontradaException(UUID id) {
        super("Doação efetivada com id " + id + " não encontrada");
    }
}
