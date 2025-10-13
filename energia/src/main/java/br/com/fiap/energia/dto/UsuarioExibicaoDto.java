package br.com.fiap.energia.dto;

import br.com.fiap.energia.model.UsuarioModel;

import java.time.LocalDate;

public record UsuarioExibicaoDto(
        Long id,
        String nome,
        String email,
        LocalDate dataCadastro
) {

    public UsuarioExibicaoDto(UsuarioModel usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCadastro()
        );
    }

}
