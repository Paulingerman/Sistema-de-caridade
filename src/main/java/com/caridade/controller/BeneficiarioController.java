package com.caridade.controller;

import com.caridade.dto.request.BeneficiarioRequestDTO;
import com.caridade.dto.response.BeneficiarioResponseDTO;
import com.caridade.service.BeneficiarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/beneficiarios")
@RequiredArgsConstructor
public class BeneficiarioController {

    private final BeneficiarioService beneficiarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeneficiarioResponseDTO criar(@RequestBody @Valid BeneficiarioRequestDTO request) {
        return beneficiarioService.criar(request);
    }

    @GetMapping
    public List<BeneficiarioResponseDTO> listar() {
        return beneficiarioService.listar();
    }

    @GetMapping("/{id}")
    public BeneficiarioResponseDTO buscarPorId(@PathVariable UUID id) {
        return beneficiarioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public BeneficiarioResponseDTO atualizar(@PathVariable UUID id, @RequestBody @Valid BeneficiarioRequestDTO request) {
        return beneficiarioService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable UUID id) {
        beneficiarioService.deletar(id);
    }
}