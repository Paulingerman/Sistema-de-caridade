package com.caridade.exception.beneficiario;

import java.util.UUID;

public class BeneficiarioNaoEncontradoException extends RuntimeException {

    public BeneficiarioNaoEncontradoException(UUID id) {
        super("Beneficiário com id " + id + " não encontrado");
    }
}