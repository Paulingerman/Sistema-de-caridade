package com.caridade.exception.solicitacao;

import java.time.LocalDateTime;

public record SolicitacaoErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}