package com.caridade.service;

import com.caridade.dto.request.SolicitacaoRequestDTO;
import com.caridade.dto.response.SolicitacaoResponseDTO;
import com.caridade.entity.StatusSolicitacao;

import java.util.List;
import java.util.UUID;

public interface SolicitacaoService {

    SolicitacaoResponseDTO criar(SolicitacaoRequestDTO request);

    List<SolicitacaoResponseDTO> listar(UUID beneficiarioId, StatusSolicitacao status);

    SolicitacaoResponseDTO buscarPorId(UUID id);

    SolicitacaoResponseDTO cancelar(UUID id);

    SolicitacaoResponseDTO concluir(UUID id);
}