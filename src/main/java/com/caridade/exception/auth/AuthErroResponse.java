package com.caridade.exception.auth;

import java.time.LocalDateTime;

public record AuthErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}