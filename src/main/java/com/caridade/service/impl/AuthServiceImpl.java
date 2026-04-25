package com.caridade.service.impl;

import com.caridade.dto.request.AuthLoginRequestDTO;
import com.caridade.dto.response.AuthLoginResponseDTO;
import com.caridade.entity.Usuario;
import com.caridade.exception.auth.CredenciaisInvalidasException;
import com.caridade.service.AuthService;
import com.caridade.service.JsonDataStoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String FILE_NAME = "usuarios.json";

    private final JsonDataStoreService jsonDataStoreService;

    public AuthServiceImpl(JsonDataStoreService jsonDataStoreService) {
        this.jsonDataStoreService = jsonDataStoreService;
    }

    @Override
    public AuthLoginResponseDTO login(AuthLoginRequestDTO request) {
        List<Usuario> usuarios = jsonDataStoreService.readList(
                FILE_NAME,
                new TypeReference<List<Usuario>>() {}
        );

        Usuario usuario = usuarios.stream()
                .filter(item -> item.getEmail().equalsIgnoreCase(request.email()))
                .findFirst()
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getSenha().equals(request.senha())) {
            throw new CredenciaisInvalidasException();
        }

        return new AuthLoginResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil()
        );
    }
}