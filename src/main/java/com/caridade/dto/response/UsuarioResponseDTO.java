package com.caridade.dto.response;

import com.caridade.entity.PerfilUsuario;
import com.caridade.entity.PrioridadeUsuario;

import java.util.UUID;

public record UsuarioResponseDTO(
        UUID id,
        String nome,
        String telefone,
        String email,
        String endereco,
        PrioridadeUsuario prioridade,
        PerfilUsuario perfil
) {
}