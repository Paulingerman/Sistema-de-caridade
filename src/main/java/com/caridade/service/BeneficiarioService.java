package com.caridade.service;

import com.caridade.dto.request.BeneficiarioRequestDTO;
import com.caridade.dto.response.BeneficiarioResponseDTO;
import com.caridade.entity.Usuario;

import java.util.List;
import java.util.UUID;

public interface BeneficiarioService {

    BeneficiarioResponseDTO criar(BeneficiarioRequestDTO request);

    List<BeneficiarioResponseDTO> listar();

    BeneficiarioResponseDTO buscarPorId(UUID id);

    BeneficiarioResponseDTO atualizar(UUID id, BeneficiarioRequestDTO request);

    void deletar(UUID id);

    void sincronizarUsuarioBeneficiario(Usuario usuario);

    void removerPorUsuarioId(UUID id);
}