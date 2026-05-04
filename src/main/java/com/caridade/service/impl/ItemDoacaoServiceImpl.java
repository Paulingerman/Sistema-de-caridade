package com.caridade.service.impl;

import com.caridade.dto.request.AtualizarStatusItemRequestDTO;
import com.caridade.dto.request.ItemDoacaoRequestDTO;
import com.caridade.dto.response.ItemDoacaoResponseDTO;
import com.caridade.entity.CategoriaItemDoacao;
import com.caridade.entity.ItemDoacao;
import com.caridade.entity.StatusItemDoacao;
import com.caridade.exception.item.ItemDoacaoNaoEncontradoException;
import com.caridade.exception.item.ItemDoacaoOperacaoInvalidaException;
import com.caridade.service.ItemDoacaoService;
import com.caridade.service.JsonDataStoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ItemDoacaoServiceImpl implements ItemDoacaoService {

    private static final String FILE_NAME = "itens.json";

    private final JsonDataStoreService jsonDataStoreService;
    private final List<ItemDoacao> itens;

    public ItemDoacaoServiceImpl(JsonDataStoreService jsonDataStoreService) {
        this.jsonDataStoreService = jsonDataStoreService;
        this.itens = new ArrayList<>(
                jsonDataStoreService.readList(FILE_NAME, new TypeReference<List<ItemDoacao>>() {})
        );
    }

    @Override
    public ItemDoacaoResponseDTO criar(ItemDoacaoRequestDTO request) {
        ItemDoacao item = ItemDoacao.builder()
                .id(UUID.randomUUID())
                .doadorId(request.doadorId())
                .nomeItem(request.nomeItem())
                .categoria(request.categoria())
                .descricao(request.descricao())
                .quantidade(request.quantidade())
                .estadoConservacao(request.estadoConservacao())
                .dataCadastro(LocalDate.now())
                .status(StatusItemDoacao.DISPONIVEL)
                .build();

        itens.add(item);
        persistir();

        return paraResponseDTO(item);
    }

    @Override
    public List<ItemDoacaoResponseDTO> listar(CategoriaItemDoacao categoria, StatusItemDoacao status) {
        return itens.stream()
                .filter(item -> categoria == null || item.getCategoria() == categoria)
                .filter(item -> status == null || item.getStatus() == status)
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public List<ItemDoacaoResponseDTO> listarDisponiveis() {
        return itens.stream()
                .filter(item -> item.getStatus() == StatusItemDoacao.DISPONIVEL)
                .filter(item -> item.getQuantidade() != null && item.getQuantidade() > 0)
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public ItemDoacaoResponseDTO buscarPorId(UUID id) {
        ItemDoacao item = buscarEntidadePorId(id);
        return paraResponseDTO(item);
    }

    @Override
    public ItemDoacaoResponseDTO atualizar(UUID id, ItemDoacaoRequestDTO request) {
        ItemDoacao item = buscarEntidadePorId(id);

        item.setDoadorId(request.doadorId());
        item.setNomeItem(request.nomeItem());
        item.setCategoria(request.categoria());
        item.setDescricao(request.descricao());
        item.setQuantidade(request.quantidade());
        item.setEstadoConservacao(request.estadoConservacao());

        persistir();

        return paraResponseDTO(item);
    }

    @Override
    public ItemDoacaoResponseDTO atualizarStatus(UUID id, AtualizarStatusItemRequestDTO request) {
        ItemDoacao item = buscarEntidadePorId(id);

        item.setStatus(request.status());
        persistir();

        return paraResponseDTO(item);
    }

    @Override
    public ItemDoacaoResponseDTO baixarQuantidade(UUID id, Integer quantidade) {
        ItemDoacao item = buscarEntidadePorId(id);

        if (quantidade == null || quantidade <= 0) {
            throw new ItemDoacaoOperacaoInvalidaException("A quantidade para baixa deve ser maior que zero");
        }

        if (item.getQuantidade() == null || quantidade > item.getQuantidade()) {
            throw new ItemDoacaoOperacaoInvalidaException("Quantidade para baixa maior que a disponível");
        }

        item.setQuantidade(item.getQuantidade() - quantidade);

        if (item.getQuantidade() == 0) {
            item.setStatus(StatusItemDoacao.ENTREGUE);
        } else if (item.getStatus() == StatusItemDoacao.RESERVADO) {
            item.setStatus(StatusItemDoacao.DISPONIVEL);
        }

        persistir();

        return paraResponseDTO(item);
    }

    @Override
    public void deletar(UUID id) {
        ItemDoacao item = buscarEntidadePorId(id);
        itens.remove(item);
        persistir();
    }

    private ItemDoacao buscarEntidadePorId(UUID id) {
        return itens.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ItemDoacaoNaoEncontradoException(id));
    }

    private ItemDoacaoResponseDTO paraResponseDTO(ItemDoacao item) {
        return new ItemDoacaoResponseDTO(
                item.getId(),
                item.getDoadorId(),
                item.getNomeItem(),
                item.getCategoria(),
                item.getDescricao(),
                item.getQuantidade(),
                item.getEstadoConservacao(),
                item.getDataCadastro(),
                item.getStatus()
        );
    }

    private void persistir() {
        jsonDataStoreService.writeList(FILE_NAME, itens);
    }
}