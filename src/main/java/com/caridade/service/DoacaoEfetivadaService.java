package com.caridade.service;

import com.caridade.dto.response.DoacaoEfetivadaResponseDTO;
import com.caridade.dto.response.RelatorioResumoDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DoacaoEfetivadaService {

    DoacaoEfetivadaResponseDTO registrar(UUID solicitacaoId, UUID itemId, UUID doadorId,
                                          UUID beneficiarioId, Integer quantidadeDoada,
                                          String nomeItem, String categoriaItem,
                                          String nomeDoador, String nomeBeneficiario,
                                          String observacoes);

    List<DoacaoEfetivadaResponseDTO> listar();

    List<DoacaoEfetivadaResponseDTO> listarPorBeneficiario(UUID beneficiarioId);

    List<DoacaoEfetivadaResponseDTO> listarPorDoador(UUID doadorId);

    List<DoacaoEfetivadaResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);

    DoacaoEfetivadaResponseDTO buscarPorId(UUID id);

    RelatorioResumoDTO gerarResumo();
}
