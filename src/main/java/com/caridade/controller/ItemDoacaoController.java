package com.caridade.controller;

import com.caridade.dto.request.AtualizarStatusItemRequestDTO;
import com.caridade.dto.request.ItemDoacaoRequestDTO;
import com.caridade.dto.response.ItemDoacaoResponseDTO;
import com.caridade.entity.CategoriaItemDoacao;
import com.caridade.entity.StatusItemDoacao;
import com.caridade.service.ItemDoacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/itens")
@RequiredArgsConstructor
public class ItemDoacaoController {

    private final ItemDoacaoService itemDoacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDoacaoResponseDTO criar(@RequestBody @Valid ItemDoacaoRequestDTO request) {
        return itemDoacaoService.criar(request);
    }

    @GetMapping
    public List<ItemDoacaoResponseDTO> listar(
            @RequestParam(required = false) CategoriaItemDoacao categoria,
            @RequestParam(required = false) StatusItemDoacao status
    ) {
        return itemDoacaoService.listar(categoria, status);
    }

    @GetMapping("/disponiveis")
    public List<ItemDoacaoResponseDTO> listarDisponiveis() {
        return itemDoacaoService.listarDisponiveis();
    }

    @GetMapping("/{id}")
    public ItemDoacaoResponseDTO buscarPorId(@PathVariable UUID id) {
        return itemDoacaoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ItemDoacaoResponseDTO atualizar(@PathVariable UUID id, @RequestBody @Valid ItemDoacaoRequestDTO request) {
        return itemDoacaoService.atualizar(id, request);
    }

    @PutMapping("/{id}/status")
    public ItemDoacaoResponseDTO atualizarStatus(
            @PathVariable UUID id,
            @RequestBody @Valid AtualizarStatusItemRequestDTO request
    ) {
        return itemDoacaoService.atualizarStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        itemDoacaoService.deletar(id);
    }
}