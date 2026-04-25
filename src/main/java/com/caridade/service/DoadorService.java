package com.caridade.service;

import com.caridade.entity.Doador;
import com.caridade.entity.Usuario;

import java.util.List;
import java.util.UUID;

public interface DoadorService {

    List<Doador> listar();

    Doador buscarPorId(UUID id);

    void sincronizarUsuarioDoador(Usuario usuario);

    void removerPorUsuarioId(UUID id);
}