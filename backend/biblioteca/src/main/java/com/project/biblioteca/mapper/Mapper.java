package com.project.biblioteca.mapper;

import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.model.UsuarioGestor;

public class Mapper {
    
    public static UsuarioGestorDto toDto(UsuarioGestor entity) {
        UsuarioGestorDto dto = new UsuarioGestorDto();
        dto.setBiblioteca(entity.getBiblioteca());
        dto.setNome(entity.getNome());
        dto.setSobrenome(entity.getSobrenome());
        dto.setEmail(entity.getEmail());
        dto.setCpf(entity.getCpf());
        dto.setTelefone(entity.getTelefone());
        return dto;
    }
}
