package com.caridade.exception.usuario;

import java.time.LocalDateTime;

public record UsuarioErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}