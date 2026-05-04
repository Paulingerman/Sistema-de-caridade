package com.caridade.dto.request;

import com.caridade.entity.CategoriaItemDoacao;
import com.caridade.entity.EstadoConservacaoItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemDoacaoRequestDTO(

        @NotNull(message = "O id do doador é obrigatório")
        UUID doadorId,

        @NotBlank(message = "O nome do item é obrigatório")
        String nomeItem,

        @NotNull(message = "A categoria é obrigatória")
        CategoriaItemDoacao categoria,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
        Integer quantidade,

        @NotNull(message = "O estado de conservação é obrigatório")
        EstadoConservacaoItem estadoConservacao
) {
}