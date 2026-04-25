package com.caridade.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Beneficiario extends Usuario {

    private TipoBeneficiario tipoBeneficiario;
    private NivelPrioridade nivelPrioridade;
}