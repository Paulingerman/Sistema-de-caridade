package com.caridade.service.impl;

import com.caridade.entity.Doador;
import com.caridade.entity.PerfilUsuario;
import com.caridade.entity.Usuario;
import com.caridade.service.DoadorService;
import com.caridade.service.JsonDataStoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoadorServiceImpl implements DoadorService {

    private static final String FILE_NAME = "doadores.json";

    private final JsonDataStoreService jsonDataStoreService;
    private final List<Doador> doadores;

    public DoadorServiceImpl(JsonDataStoreService jsonDataStoreService) {
        this.jsonDataStoreService = jsonDataStoreService;
        this.doadores = new ArrayList<>(
                jsonDataStoreService.readList(FILE_NAME, new TypeReference<List<Doador>>() {})
        );
    }

    @Override
    public List<Doador> listar() {
        return List.copyOf(doadores);
    }

    @Override
    public Doador buscarPorId(UUID id) {
        return doadores.stream()
                .filter(doador -> doador.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Doador com id " + id + " não encontrado"));
    }

    @Override
    public void sincronizarUsuarioDoador(Usuario usuario) {
        if (usuario.getPerfil() != PerfilUsuario.DOADOR) {
            return;
        }

        Doador doador = doadores.stream()
                .filter(d -> d.getId().equals(usuario.getId()))
                .findFirst()
                .orElseGet(() -> {
                    Doador novo = Doador.builder()
                            .id(usuario.getId())
                            .build();
                    doadores.add(novo);
                    return novo;
                });

        doador.setNome(usuario.getNome());
        doador.setTelefone(usuario.getTelefone());
        doador.setEmail(usuario.getEmail());
        doador.setEndereco(usuario.getEndereco());
        doador.setSenha(usuario.getSenha());
        doador.setPerfil(PerfilUsuario.DOADOR);
        doador.setPrioridade(usuario.getPrioridade());

        persistir();
    }

    @Override
    public void removerPorUsuarioId(UUID id) {
        doadores.removeIf(doador -> doador.getId().equals(id));
        persistir();
    }

    private void persistir() {
        jsonDataStoreService.writeList(FILE_NAME, doadores);
    }
}
