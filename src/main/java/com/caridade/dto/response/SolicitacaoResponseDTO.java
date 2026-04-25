package com.caridade.dto.response;

import com.caridade.entity.StatusSolicitacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitacaoResponseDTO(
        UUID id,
        UUID beneficiarioId,
        UUID itemId,
        Integer quantidadeSolicitada,
        String justificativa,
        StatusSolicitacao status,
        String observacao,
        LocalDateTime dataSolicitacao
) {
}