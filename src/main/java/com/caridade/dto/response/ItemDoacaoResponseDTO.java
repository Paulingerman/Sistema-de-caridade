package com.caridade.dto.response;

import com.caridade.entity.CategoriaItemDoacao;
import com.caridade.entity.EstadoConservacaoItem;
import com.caridade.entity.StatusItemDoacao;

import java.time.LocalDate;
import java.util.UUID;

public record ItemDoacaoResponseDTO(
        UUID id,
        String nomeItem,
        CategoriaItemDoacao categoria,
        String descricao,
        Integer quantidade,
        EstadoConservacaoItem estadoConservacao,
        LocalDate dataCadastro,
        StatusItemDoacao status
) {
}