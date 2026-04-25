package com.caridade.service;

import com.caridade.dto.request.AtualizarStatusItemRequestDTO;
import com.caridade.dto.request.ItemDoacaoRequestDTO;
import com.caridade.dto.response.ItemDoacaoResponseDTO;
import com.caridade.entity.CategoriaItemDoacao;
import com.caridade.entity.StatusItemDoacao;

import java.util.List;
import java.util.UUID;

public interface ItemDoacaoService {

    ItemDoacaoResponseDTO criar(ItemDoacaoRequestDTO request);

    List<ItemDoacaoResponseDTO> listar(CategoriaItemDoacao categoria, StatusItemDoacao status);

    List<ItemDoacaoResponseDTO> listarDisponiveis();

    ItemDoacaoResponseDTO buscarPorId(UUID id);

    ItemDoacaoResponseDTO atualizar(UUID id, ItemDoacaoRequestDTO request);

    ItemDoacaoResponseDTO atualizarStatus(UUID id, AtualizarStatusItemRequestDTO request);

    ItemDoacaoResponseDTO baixarQuantidade(UUID id, Integer quantidade);

    void deletar(UUID id);
}