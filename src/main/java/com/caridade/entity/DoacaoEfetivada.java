package com.caridade.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoacaoEfetivada {

    private UUID id;
    private UUID itemId;
    private UUID doadorId;
    private UUID beneficiarioId;
    private UUID solicitacaoId;
    private String nomeItem;
    private String categoriaItem;
    private String nomeDoador;
    private String nomeBeneficiario;
    private Integer quantidadeDoada;
    private LocalDateTime dataEfetivacao;
    private String observacoes;
}
