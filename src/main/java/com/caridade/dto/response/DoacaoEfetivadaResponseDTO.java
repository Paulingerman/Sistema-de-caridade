package com.caridade.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record DoacaoEfetivadaResponseDTO(
        UUID id,
        UUID itemId,
        UUID doadorId,
        UUID beneficiarioId,
        UUID solicitacaoId,
        String nomeItem,
        String categoriaItem,
        String nomeDoador,
        String nomeBeneficiario,
        Integer quantidadeDoada,
        LocalDateTime dataEfetivacao,
        String observacoes
) {
}
