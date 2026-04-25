package com.caridade.exception.beneficiario;

public class BeneficiarioJaExisteException extends RuntimeException {

    public BeneficiarioJaExisteException(String email) {
        super("Já existe um beneficiário cadastrado com o e-mail: " + email);
    }
}