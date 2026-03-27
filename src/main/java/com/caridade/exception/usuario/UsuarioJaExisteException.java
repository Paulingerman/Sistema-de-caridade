package com.caridade.exception.usuario;

public class UsuarioJaExisteException extends RuntimeException {

    public UsuarioJaExisteException(String email) {
        super("Já existe um usuário cadastrado com o e-mail: " + email);
    }
}