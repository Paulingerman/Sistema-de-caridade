package com.caridade.exception.doacao;

import java.time.LocalDateTime;

public record DoacaoEfetivadaErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}
