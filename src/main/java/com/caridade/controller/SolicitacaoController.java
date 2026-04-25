package com.caridade.controller;

import com.caridade.dto.request.SolicitacaoRequestDTO;
import com.caridade.dto.response.SolicitacaoResponseDTO;
import com.caridade.entity.StatusSolicitacao;
import com.caridade.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitacaoResponseDTO criar(@RequestBody @Valid SolicitacaoRequestDTO request) {
        return solicitacaoService.criar(request);
    }

    @GetMapping
    public List<SolicitacaoResponseDTO> listar(
            @RequestParam(required = false) UUID beneficiarioId,
            @RequestParam(required = false) StatusSolicitacao status
    ) {
        return solicitacaoService.listar(beneficiarioId, status);
    }

    @GetMapping("/{id}")
    public SolicitacaoResponseDTO buscarPorId(@PathVariable UUID id) {
        return solicitacaoService.buscarPorId(id);
    }

    @PutMapping("/{id}/cancelar")
    public SolicitacaoResponseDTO cancelar(@PathVariable UUID id) {
        return solicitacaoService.cancelar(id);
    }

    @PutMapping("/{id}/concluir")
    public SolicitacaoResponseDTO concluir(@PathVariable UUID id) {
        return solicitacaoService.concluir(id);
    }
}