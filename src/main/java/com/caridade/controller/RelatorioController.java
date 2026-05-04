package com.caridade.controller;

import com.caridade.dto.response.DoacaoEfetivadaResponseDTO;
import com.caridade.dto.response.RelatorioResumoDTO;
import com.caridade.service.DoacaoEfetivadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final DoacaoEfetivadaService doacaoEfetivadaService;

    @GetMapping("/doacoes")
    public List<DoacaoEfetivadaResponseDTO> listarDoacoes() {
        return doacaoEfetivadaService.listar();
    }

    @GetMapping("/doacoes/{id}")
    public DoacaoEfetivadaResponseDTO buscarPorId(@PathVariable UUID id) {
        return doacaoEfetivadaService.buscarPorId(id);
    }

    @GetMapping("/doacoes/beneficiario/{beneficiarioId}")
    public List<DoacaoEfetivadaResponseDTO> listarPorBeneficiario(@PathVariable UUID beneficiarioId) {
        return doacaoEfetivadaService.listarPorBeneficiario(beneficiarioId);
    }

    @GetMapping("/doacoes/doador/{doadorId}")
    public List<DoacaoEfetivadaResponseDTO> listarPorDoador(@PathVariable UUID doadorId) {
        return doacaoEfetivadaService.listarPorDoador(doadorId);
    }

    @GetMapping("/doacoes/periodo")
    public List<DoacaoEfetivadaResponseDTO> listarPorPeriodo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        return doacaoEfetivadaService.listarPorPeriodo(dataInicio, dataFim);
    }

    @GetMapping("/resumo")
    public RelatorioResumoDTO gerarResumo() {
        return doacaoEfetivadaService.gerarResumo();
    }
}
