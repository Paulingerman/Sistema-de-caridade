package com.caridade.service.impl;

import com.caridade.dto.request.UsuarioRequestDTO;
import com.caridade.dto.response.UsuarioResponseDTO;
import com.caridade.entity.PerfilUsuario;
import com.caridade.entity.Usuario;
import com.caridade.exception.usuario.UsuarioJaExisteException;
import com.caridade.exception.usuario.UsuarioNaoEncontradoException;
import com.caridade.service.BeneficiarioService;
import com.caridade.service.DoadorService;
import com.caridade.service.JsonDataStoreService;
import com.caridade.service.UsuarioService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final String FILE_NAME = "usuarios.json";

    private final JsonDataStoreService jsonDataStoreService;
    private final BeneficiarioService beneficiarioService;
    private final DoadorService doadorService;
    private final List<Usuario> usuarios;

    public UsuarioServiceImpl(
            JsonDataStoreService jsonDataStoreService,
            BeneficiarioService beneficiarioService,
            DoadorService doadorService
    ) {
        this.jsonDataStoreService = jsonDataStoreService;
        this.beneficiarioService = beneficiarioService;
        this.doadorService = doadorService;
        this.usuarios = new ArrayList<>(
                jsonDataStoreService.readList(FILE_NAME, new TypeReference<List<Usuario>>() {})
        );
    }

    @Override
    public UsuarioResponseDTO criar(UsuarioRequestDTO request) {
        validarEmailDuplicado(request.email(), null);

        Usuario usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nome(request.nome())
                .telefone(request.telefone())
                .email(request.email())
                .endereco(request.endereco())
                .senha(request.senha())
                .prioridade(request.prioridade())
                .perfil(request.perfil())
                .build();

        usuarios.add(usuario);
        persistir();
        sincronizarPerfis(usuario);

        return paraResponseDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listar() {
        return usuarios.stream()
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public UsuarioResponseDTO buscarPorId(UUID id) {
        Usuario usuario = buscarEntidadePorId(id);
        return paraResponseDTO(usuario);
    }

    @Override
    public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO request) {
        Usuario usuario = buscarEntidadePorId(id);
        PerfilUsuario perfilAnterior = usuario.getPerfil();

        validarEmailDuplicado(request.email(), id);

        usuario.setNome(request.nome());
        usuario.setTelefone(request.telefone());
        usuario.setEmail(request.email());
        usuario.setEndereco(request.endereco());
        usuario.setSenha(request.senha());
        usuario.setPrioridade(request.prioridade());
        usuario.setPerfil(request.perfil());

        persistir();
        removerRelacionamentosAntigos(id, perfilAnterior, usuario.getPerfil());
        sincronizarPerfis(usuario);

        return paraResponseDTO(usuario);
    }

    @Override
    public void deletar(UUID id) {
        Usuario usuario = buscarEntidadePorId(id);

        usuarios.remove(usuario);
        persistir();

        if (usuario.getPerfil() == PerfilUsuario.BENEFICIARIO) {
            beneficiarioService.removerPorUsuarioId(id);
        }

        if (usuario.getPerfil() == PerfilUsuario.DOADOR) {
            doadorService.removerPorUsuarioId(id);
        }
    }

    private void sincronizarPerfis(Usuario usuario) {
        if (usuario.getPerfil() == PerfilUsuario.BENEFICIARIO) {
            beneficiarioService.sincronizarUsuarioBeneficiario(usuario);
        }

        if (usuario.getPerfil() == PerfilUsuario.DOADOR) {
            doadorService.sincronizarUsuarioDoador(usuario);
        }
    }

    private void removerRelacionamentosAntigos(UUID id, PerfilUsuario perfilAnterior, PerfilUsuario perfilNovo) {
        if (perfilAnterior == perfilNovo) {
            return;
        }

        if (perfilAnterior == PerfilUsuario.BENEFICIARIO) {
            beneficiarioService.removerPorUsuarioId(id);
        }

        if (perfilAnterior == PerfilUsuario.DOADOR) {
            doadorService.removerPorUsuarioId(id);
        }
    }

    private Usuario buscarEntidadePorId(UUID id) {
        return usuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    private void validarEmailDuplicado(String email, UUID idIgnorado) {
        boolean emailJaCadastrado = usuarios.stream()
                .anyMatch(usuario ->
                        usuario.getEmail().equalsIgnoreCase(email) &&
                        (idIgnorado == null || !usuario.getId().equals(idIgnorado))
                );

        if (emailJaCadastrado) {
            throw new UsuarioJaExisteException(email);
        }
    }

    private UsuarioResponseDTO paraResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getTelefone(),
                usuario.getEmail(),
                usuario.getEndereco(),
                usuario.getPrioridade(),
                usuario.getPerfil()
        );
    }

    private void persistir() {
        jsonDataStoreService.writeList(FILE_NAME, usuarios);
    }
}