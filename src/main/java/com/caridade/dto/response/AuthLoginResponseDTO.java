package com.caridade.dto.response;

import com.caridade.entity.PerfilUsuario;

import java.util.UUID;

public record AuthLoginResponseDTO(
        UUID id,
        String nome,
        String email,
        PerfilUsuario perfil
) {
}