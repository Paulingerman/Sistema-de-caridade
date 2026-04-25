
package com.caridade.exception.beneficiario;

import java.time.LocalDateTime;

public record BeneficiarioErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem
) {
}