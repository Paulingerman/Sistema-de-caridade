package com.caridade.dto.request;

import com.caridade.entity.NivelPrioridade;
import com.caridade.entity.TipoBeneficiario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BeneficiarioRequestDTO(

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,

        @NotNull(message = "O tipo de beneficiário é obrigatório")
        TipoBeneficiario tipoBeneficiario,

        @NotNull(message = "O nível de prioridade é obrigatório")
        NivelPrioridade nivelPrioridade
) {
}