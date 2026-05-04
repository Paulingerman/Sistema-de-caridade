package com.caridade.service.impl;

import com.caridade.dto.response.DoacaoEfetivadaResponseDTO;
import com.caridade.dto.response.RelatorioResumoDTO;
import com.caridade.entity.DoacaoEfetivada;
import com.caridade.exception.doacao.DoacaoEfetivadaNaoEncontradaException;
import com.caridade.service.DoacaoEfetivadaService;
import com.caridade.service.JsonDataStoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoacaoEfetivadaServiceImpl implements DoacaoEfetivadaService {

    private static final String FILE_NAME = "doacoes_efetivadas.json";

    private final JsonDataStoreService jsonDataStoreService;
    private final List<DoacaoEfetivada> doacoes;

    public DoacaoEfetivadaServiceImpl(JsonDataStoreService jsonDataStoreService) {
        this.jsonDataStoreService = jsonDataStoreService;
        this.doacoes = new ArrayList<>(
                jsonDataStoreService.readList(FILE_NAME, new TypeReference<List<DoacaoEfetivada>>() {})
        );
    }

    @Override
    public DoacaoEfetivadaResponseDTO registrar(UUID solicitacaoId, UUID itemId, UUID doadorId,
                                                 UUID beneficiarioId, Integer quantidadeDoada,
                                                 String nomeItem, String categoriaItem,
                                                 String nomeDoador, String nomeBeneficiario,
                                                 String observacoes) {

        DoacaoEfetivada doacao = DoacaoEfetivada.builder()
                .id(UUID.randomUUID())
                .solicitacaoId(solicitacaoId)
                .itemId(itemId)
                .doadorId(doadorId)
                .beneficiarioId(beneficiarioId)
                .quantidadeDoada(quantidadeDoada)
                .nomeItem(nomeItem)
                .categoriaItem(categoriaItem)
                .nomeDoador(nomeDoador)
                .nomeBeneficiario(nomeBeneficiario)
                .dataEfetivacao(LocalDateTime.now())
                .observacoes(observacoes)
                .build();

        doacoes.add(doacao);
        persistir();

        return paraResponseDTO(doacao);
    }

    @Override
    public List<DoacaoEfetivadaResponseDTO> listar() {
        return doacoes.stream()
                .sorted(Comparator.comparing(DoacaoEfetivada::getDataEfetivacao).reversed())
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public List<DoacaoEfetivadaResponseDTO> listarPorBeneficiario(UUID beneficiarioId) {
        return doacoes.stream()
                .filter(doacao -> doacao.getBeneficiarioId().equals(beneficiarioId))
                .sorted(Comparator.comparing(DoacaoEfetivada::getDataEfetivacao).reversed())
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public List<DoacaoEfetivadaResponseDTO> listarPorDoador(UUID doadorId) {
        return doacoes.stream()
                .filter(doacao -> doacao.getDoadorId().equals(doadorId))
                .sorted(Comparator.comparing(DoacaoEfetivada::getDataEfetivacao).reversed())
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public List<DoacaoEfetivadaResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return doacoes.stream()
                .filter(doacao -> {
                    LocalDate dataDoacao = doacao.getDataEfetivacao().toLocalDate();
                    boolean depoisDoInicio = dataInicio == null || !dataDoacao.isBefore(dataInicio);
                    boolean antesDoFim = dataFim == null || !dataDoacao.isAfter(dataFim);
                    return depoisDoInicio && antesDoFim;
                })
                .sorted(Comparator.comparing(DoacaoEfetivada::getDataEfetivacao).reversed())
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public DoacaoEfetivadaResponseDTO buscarPorId(UUID id) {
        DoacaoEfetivada doacao = buscarEntidadePorId(id);
        return paraResponseDTO(doacao);
    }

    @Override
    public RelatorioResumoDTO gerarResumo() {
        long totalDoacoes = doacoes.size();

        long totalItens = doacoes.stream()
                .mapToLong(d -> d.getQuantidadeDoada() != null ? d.getQuantidadeDoada() : 0)
                .sum();

        long totalDoadores = doacoes.stream()
                .map(DoacaoEfetivada::getDoadorId)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        long totalBeneficiarios = doacoes.stream()
                .map(DoacaoEfetivada::getBeneficiarioId)
                .distinct()
                .count();

        Map<String, Long> porCategoria = doacoes.stream()
                .collect(Collectors.groupingBy(
                        doacao -> doacao.getCategoriaItem() != null
                                ? doacao.getCategoriaItem()
                                : "NAO_IDENTIFICADA",
                        Collectors.counting()
                ));

        List<DoacaoEfetivadaResponseDTO> ultimas = doacoes.stream()
                .sorted(Comparator.comparing(DoacaoEfetivada::getDataEfetivacao).reversed())
                .limit(10)
                .map(this::paraResponseDTO)
                .toList();

        return new RelatorioResumoDTO(
                totalDoacoes,
                totalItens,
                totalDoadores,
                totalBeneficiarios,
                porCategoria,
                ultimas
        );
    }

    private DoacaoEfetivada buscarEntidadePorId(UUID id) {
        return doacoes.stream()
                .filter(doacao -> doacao.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new DoacaoEfetivadaNaoEncontradaException(id));
    }

    private DoacaoEfetivadaResponseDTO paraResponseDTO(DoacaoEfetivada doacao) {
        return new DoacaoEfetivadaResponseDTO(
                doacao.getId(),
                doacao.getItemId(),
                doacao.getDoadorId(),
                doacao.getBeneficiarioId(),
                doacao.getSolicitacaoId(),
                doacao.getNomeItem(),
                doacao.getCategoriaItem(),
                doacao.getNomeDoador(),
                doacao.getNomeBeneficiario(),
                doacao.getQuantidadeDoada(),
                doacao.getDataEfetivacao(),
                doacao.getObservacoes()
        );
    }

    private void persistir() {
        jsonDataStoreService.writeList(FILE_NAME, doacoes);
    }
}
