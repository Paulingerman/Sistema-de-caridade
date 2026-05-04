package com.caridade.dto.response;

import java.util.List;
import java.util.Map;

public record RelatorioResumoDTO(
        long totalDoacoesEfetivadas,
        long totalItensDoados,
        long totalDoadoresAtivos,
        long totalBeneficiariosAtendidos,
        Map<String, Long> doacoesPorCategoria,
        List<DoacaoEfetivadaResponseDTO> ultimasDoacoes
) {
}
