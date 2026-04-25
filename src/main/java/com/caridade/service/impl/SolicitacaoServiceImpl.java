package com.caridade.service.impl;

import com.caridade.dto.request.AtualizarStatusItemRequestDTO;
import com.caridade.dto.request.SolicitacaoRequestDTO;
import com.caridade.dto.response.ItemDoacaoResponseDTO;
import com.caridade.dto.response.SolicitacaoResponseDTO;
import com.caridade.entity.Solicitacao;
import com.caridade.entity.StatusItemDoacao;
import com.caridade.entity.StatusSolicitacao;
import com.caridade.exception.solicitacao.SolicitacaoNaoEncontradaException;
import com.caridade.exception.solicitacao.SolicitacaoOperacaoInvalidaException;
import com.caridade.service.BeneficiarioService;
import com.caridade.service.ItemDoacaoService;
import com.caridade.service.JsonDataStoreService;
import com.caridade.service.SolicitacaoService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private static final String FILE_NAME = "solicitacoes.json";

    private final ItemDoacaoService itemDoacaoService;
    private final BeneficiarioService beneficiarioService;
    private final JsonDataStoreService jsonDataStoreService;
    private final List<Solicitacao> solicitacoes;

    public SolicitacaoServiceImpl(
            ItemDoacaoService itemDoacaoService,
            BeneficiarioService beneficiarioService,
            JsonDataStoreService jsonDataStoreService
    ) {
        this.itemDoacaoService = itemDoacaoService;
        this.beneficiarioService = beneficiarioService;
        this.jsonDataStoreService = jsonDataStoreService;
        this.solicitacoes = new ArrayList<>(
                jsonDataStoreService.readList(FILE_NAME, new TypeReference<List<Solicitacao>>() {})
        );
    }

    @Override
    public SolicitacaoResponseDTO criar(SolicitacaoRequestDTO request) {
        beneficiarioService.buscarPorId(request.beneficiarioId());

        ItemDoacaoResponseDTO item = itemDoacaoService.buscarPorId(request.itemId());

        StatusSolicitacao status;
        String observacao;

        if (item.status() != StatusItemDoacao.DISPONIVEL) {
            status = StatusSolicitacao.REJEITADA;
            observacao = "Solicitação rejeitada: item indisponível para solicitação";
        } else if (request.quantidadeSolicitada() > item.quantidade()) {
            status = StatusSolicitacao.REJEITADA;
            observacao = "Solicitação rejeitada: quantidade solicitada maior que a disponível";
        } else {
            status = StatusSolicitacao.APROVADA;
            observacao = "Solicitação aprovada com base na disponibilidade";

            if (request.quantidadeSolicitada().equals(item.quantidade())) {
                itemDoacaoService.atualizarStatus(
                        item.id(),
                        new AtualizarStatusItemRequestDTO(StatusItemDoacao.RESERVADO)
                );
            }
        }

        Solicitacao solicitacao = Solicitacao.builder()
                .id(UUID.randomUUID())
                .beneficiarioId(request.beneficiarioId())
                .itemId(request.itemId())
                .quantidadeSolicitada(request.quantidadeSolicitada())
                .justificativa(request.justificativa())
                .status(status)
                .observacao(observacao)
                .dataSolicitacao(LocalDateTime.now())
                .build();

        solicitacoes.add(solicitacao);
        persistir();

        return paraResponseDTO(solicitacao);
    }

    @Override
    public List<SolicitacaoResponseDTO> listar(UUID beneficiarioId, StatusSolicitacao status) {
        return solicitacoes.stream()
                .filter(solicitacao -> beneficiarioId == null || solicitacao.getBeneficiarioId().equals(beneficiarioId))
                .filter(solicitacao -> status == null || solicitacao.getStatus() == status)
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public SolicitacaoResponseDTO buscarPorId(UUID id) {
        Solicitacao solicitacao = buscarEntidadePorId(id);
        return paraResponseDTO(solicitacao);
    }

    @Override
    public SolicitacaoResponseDTO cancelar(UUID id) {
        Solicitacao solicitacao = buscarEntidadePorId(id);

        if (solicitacao.getStatus() == StatusSolicitacao.CONCLUIDA) {
            throw new SolicitacaoOperacaoInvalidaException("Não é possível cancelar uma solicitação já concluída");
        }

        solicitacao.setStatus(StatusSolicitacao.CANCELADA);
        solicitacao.setObservacao("Solicitação cancelada");

        ItemDoacaoResponseDTO item = itemDoacaoService.buscarPorId(solicitacao.getItemId());

        if (item.status() == StatusItemDoacao.RESERVADO) {
            itemDoacaoService.atualizarStatus(
                    item.id(),
                    new AtualizarStatusItemRequestDTO(StatusItemDoacao.DISPONIVEL)
            );
        }

        persistir();

        return paraResponseDTO(solicitacao);
    }

    @Override
    public SolicitacaoResponseDTO concluir(UUID id) {
        Solicitacao solicitacao = buscarEntidadePorId(id);

        if (solicitacao.getStatus() != StatusSolicitacao.APROVADA) {
            throw new SolicitacaoOperacaoInvalidaException("Apenas solicitações aprovadas podem ser concluídas");
        }

        itemDoacaoService.baixarQuantidade(
                solicitacao.getItemId(),
                solicitacao.getQuantidadeSolicitada()
        );

        solicitacao.setStatus(StatusSolicitacao.CONCLUIDA);
        solicitacao.setObservacao("Solicitação concluída e estoque atualizado");

        persistir();

        return paraResponseDTO(solicitacao);
    }

    private Solicitacao buscarEntidadePorId(UUID id) {
        return solicitacoes.stream()
                .filter(solicitacao -> solicitacao.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException(id));
    }

    private SolicitacaoResponseDTO paraResponseDTO(Solicitacao solicitacao) {
        return new SolicitacaoResponseDTO(
                solicitacao.getId(),
                solicitacao.getBeneficiarioId(),
                solicitacao.getItemId(),
                solicitacao.getQuantidadeSolicitada(),
                solicitacao.getJustificativa(),
                solicitacao.getStatus(),
                solicitacao.getObservacao(),
                solicitacao.getDataSolicitacao()
        );
    }

    private void persistir() {
        jsonDataStoreService.writeList(FILE_NAME, solicitacoes);
    }
}