package com.caridade.exception.item;

import java.util.UUID;

public class ItemDoacaoNaoEncontradoException extends RuntimeException {

    public ItemDoacaoNaoEncontradoException(UUID id) {
        super("Item de doação com id " + id + " não encontrado");
    }
}