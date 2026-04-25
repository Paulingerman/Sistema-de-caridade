package com.caridade.service;

import com.caridade.dto.request.UsuarioRequestDTO;
import com.caridade.dto.response.UsuarioResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {

    UsuarioResponseDTO criar(UsuarioRequestDTO request);

    List<UsuarioResponseDTO> listar();

    UsuarioResponseDTO buscarPorId(UUID id);

    UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO request);

    void deletar(UUID id);
}