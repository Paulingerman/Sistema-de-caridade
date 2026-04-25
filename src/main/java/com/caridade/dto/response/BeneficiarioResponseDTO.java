package com.caridade.dto.response;

import com.caridade.entity.NivelPrioridade;
import com.caridade.entity.TipoBeneficiario;

import java.util.UUID;

public record BeneficiarioResponseDTO(
        UUID id,
        String nome,
        String telefone,
        String email,
        String endereco,
        TipoBeneficiario tipoBeneficiario,
        NivelPrioridade nivelPrioridade
) {
}