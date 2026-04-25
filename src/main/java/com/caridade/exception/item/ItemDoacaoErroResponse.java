package com.caridade.exception.item;

import java.time.LocalDateTime;

public record ItemDoacaoErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}