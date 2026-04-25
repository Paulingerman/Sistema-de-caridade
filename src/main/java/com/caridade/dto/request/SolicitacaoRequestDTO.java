package com.caridade.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SolicitacaoRequestDTO(

        @NotNull(message = "O id do beneficiário é obrigatório")
        UUID beneficiarioId,

        @NotNull(message = "O id do item é obrigatório")
        UUID itemId,

        @NotNull(message = "A quantidade solicitada é obrigatória")
        @Min(value = 1, message = "A quantidade solicitada deve ser no mínimo 1")
        Integer quantidadeSolicitada,

        @NotBlank(message = "A justificativa é obrigatória")
        String justificativa
) {
}