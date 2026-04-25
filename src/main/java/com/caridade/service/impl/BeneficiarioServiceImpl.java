package com.caridade.service.impl;

import com.caridade.dto.request.BeneficiarioRequestDTO;
import com.caridade.dto.response.BeneficiarioResponseDTO;
import com.caridade.entity.Beneficiario;
import com.caridade.entity.NivelPrioridade;
import com.caridade.entity.PerfilUsuario;
import com.caridade.entity.PrioridadeUsuario;
import com.caridade.entity.TipoBeneficiario;
import com.caridade.entity.Usuario;
import com.caridade.exception.beneficiario.BeneficiarioJaExisteException;
import com.caridade.exception.beneficiario.BeneficiarioNaoEncontradoException;
import com.caridade.service.BeneficiarioService;
import com.caridade.service.JsonDataStoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BeneficiarioServiceImpl implements BeneficiarioService {

    private static final String FILE_NAME = "beneficiarios.json";

    private final JsonDataStoreService jsonDataStoreService;
    private final List<Beneficiario> beneficiarios;

    public BeneficiarioServiceImpl(JsonDataStoreService jsonDataStoreService) {
        this.jsonDataStoreService = jsonDataStoreService;
        this.beneficiarios = new ArrayList<>(
                jsonDataStoreService.readList(FILE_NAME, new TypeReference<List<Beneficiario>>() {})
        );
    }

    @Override
    public BeneficiarioResponseDTO criar(BeneficiarioRequestDTO request) {
        validarEmailDuplicado(request.email(), null);

        Beneficiario beneficiario = Beneficiario.builder()
                .id(UUID.randomUUID())
                .nome(request.nome())
                .telefone(request.telefone())
                .email(request.email())
                .endereco(request.endereco())
                .senha(request.senha())
                .perfil(PerfilUsuario.BENEFICIARIO)
                .prioridade(PrioridadeUsuario.valueOf(request.nivelPrioridade().name()))
                .tipoBeneficiario(request.tipoBeneficiario())
                .nivelPrioridade(request.nivelPrioridade())
                .build();

        beneficiarios.add(beneficiario);
        persistir();

        return paraResponseDTO(beneficiario);
    }

    @Override
    public List<BeneficiarioResponseDTO> listar() {
        return beneficiarios.stream()
                .map(this::paraResponseDTO)
                .toList();
    }

    @Override
    public BeneficiarioResponseDTO buscarPorId(UUID id) {
        Beneficiario beneficiario = buscarEntidadePorId(id);
        return paraResponseDTO(beneficiario);
    }

    @Override
    public BeneficiarioResponseDTO atualizar(UUID id, BeneficiarioRequestDTO request) {
        Beneficiario beneficiario = buscarEntidadePorId(id);

        validarEmailDuplicado(request.email(), id);

        beneficiario.setNome(request.nome());
        beneficiario.setTelefone(request.telefone());
        beneficiario.setEmail(request.email());
        beneficiario.setEndereco(request.endereco());
        beneficiario.setSenha(request.senha());
        beneficiario.setTipoBeneficiario(request.tipoBeneficiario());
        beneficiario.setNivelPrioridade(request.nivelPrioridade());
        beneficiario.setPrioridade(PrioridadeUsuario.valueOf(request.nivelPrioridade().name()));
        beneficiario.setPerfil(PerfilUsuario.BENEFICIARIO);

        persistir();

        return paraResponseDTO(beneficiario);
    }

    @Override
    public void deletar(UUID id) {
        Beneficiario beneficiario = buscarEntidadePorId(id);
        beneficiarios.remove(beneficiario);
        persistir();
    }

    @Override
    public void sincronizarUsuarioBeneficiario(Usuario usuario) {
        if (usuario.getPerfil() != PerfilUsuario.BENEFICIARIO) {
            return;
        }

        Beneficiario beneficiario = beneficiarios.stream()
                .filter(item -> item.getId().equals(usuario.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Beneficiario novo = Beneficiario.builder()
                            .id(usuario.getId())
                            .tipoBeneficiario(TipoBeneficiario.FAMILIA)
                            .nivelPrioridade(mapNivel(usuario.getPrioridade()))
                            .build();
                    beneficiarios.add(novo);
                    return novo;
                });

        beneficiario.setNome(usuario.getNome());
        beneficiario.setTelefone(usuario.getTelefone());
        beneficiario.setEmail(usuario.getEmail());
        beneficiario.setEndereco(usuario.getEndereco());
        beneficiario.setSenha(usuario.getSenha());
        beneficiario.setPerfil(PerfilUsuario.BENEFICIARIO);
        beneficiario.setPrioridade(usuario.getPrioridade());

        if (beneficiario.getTipoBeneficiario() == null) {
            beneficiario.setTipoBeneficiario(TipoBeneficiario.FAMILIA);
        }

        if (beneficiario.getNivelPrioridade() == null) {
            beneficiario.setNivelPrioridade(mapNivel(usuario.getPrioridade()));
        }

        persistir();
    }

    @Override
    public void removerPorUsuarioId(UUID id) {
        beneficiarios.removeIf(beneficiario -> beneficiario.getId().equals(id));
        persistir();
    }

    private Beneficiario buscarEntidadePorId(UUID id) {
        return beneficiarios.stream()
                .filter(beneficiario -> beneficiario.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BeneficiarioNaoEncontradoException(id));
    }

    private void validarEmailDuplicado(String email, UUID idIgnorado) {
        boolean emailJaCadastrado = beneficiarios.stream()
                .anyMatch(beneficiario ->
                        beneficiario.getEmail().equalsIgnoreCase(email) &&
                        (idIgnorado == null || !beneficiario.getId().equals(idIgnorado))
                );

        if (emailJaCadastrado) {
            throw new BeneficiarioJaExisteException(email);
        }
    }

    private NivelPrioridade mapNivel(PrioridadeUsuario prioridadeUsuario) {
        return NivelPrioridade.valueOf(prioridadeUsuario.name());
    }

    private BeneficiarioResponseDTO paraResponseDTO(Beneficiario beneficiario) {
        return new BeneficiarioResponseDTO(
                beneficiario.getId(),
                beneficiario.getNome(),
                beneficiario.getTelefone(),
                beneficiario.getEmail(),
                beneficiario.getEndereco(),
                beneficiario.getTipoBeneficiario(),
                beneficiario.getNivelPrioridade()
        );
    }

    private void persistir() {
        jsonDataStoreService.writeList(FILE_NAME, beneficiarios);
    }
}