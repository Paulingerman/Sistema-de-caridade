package com.caridade.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDoacao {

    private UUID id;
    private UUID doadorId;
    private String nomeItem;
    private CategoriaItemDoacao categoria;
    private String descricao;
    private Integer quantidade;
    private EstadoConservacaoItem estadoConservacao;
    private LocalDate dataCadastro;
    private StatusItemDoacao status;
}