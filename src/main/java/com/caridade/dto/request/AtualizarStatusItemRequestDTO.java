package com.caridade.dto.request;

import com.caridade.entity.StatusItemDoacao;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusItemRequestDTO(

        @NotNull(message = "O status é obrigatório")
        StatusItemDoacao status
) {
}