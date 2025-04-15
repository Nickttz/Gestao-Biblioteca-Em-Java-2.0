package com.project.biblioteca.dto;

import com.project.biblioteca.model.Biblioteca;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioGestorDto {
    
    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;
    private Biblioteca biblioteca;
}
