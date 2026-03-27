package com.caridade.service.impl;

import com.caridade.dto.request.UsuarioRequestDTO;
import com.caridade.dto.response.UsuarioResponseDTO;
import com.caridade.exception.usuario.UsuarioJaExisteException;
import com.caridade.exception.usuario.UsuarioNaoEncontradoException;
import com.caridade.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final List<UsuarioResponseDTO> usuarios = new ArrayList<>();

    @Override
    public UsuarioResponseDTO criar(UsuarioRequestDTO request) {
        boolean emailJaCadastrado = usuarios.stream()
                .anyMatch(usuario -> usuario.email().equalsIgnoreCase(request.email()));

        if (emailJaCadastrado) {
            throw new UsuarioJaExisteException(request.email());
        }

        UsuarioResponseDTO usuario = new UsuarioResponseDTO(
                UUID.randomUUID(),
                request.nome(),
                request.email(),
                request.prioridade()
        );

        usuarios.add(usuario);
        return usuario;
    }

    @Override
    public List<UsuarioResponseDTO> listar() {
        return usuarios;
    }

    @Override
    public UsuarioResponseDTO buscarPorId(UUID id) {
        return usuarios.stream()
                .filter(usuario -> usuario.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    @Override
    public void deletar(UUID id) {
        UsuarioResponseDTO usuario = buscarPorId(id);
        usuarios.remove(usuario);
    }
}