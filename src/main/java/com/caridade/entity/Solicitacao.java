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
public class Solicitacao {

    private UUID id;
    private UUID beneficiarioId;
    private UUID itemId;
    private Integer quantidadeSolicitada;
    private String justificativa;
    private StatusSolicitacao status;
    private String observacao;
    private LocalDateTime dataSolicitacao;
}