package com.project.biblioteca.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioGestorDto {
    
    private String email;
    private String senha;

    public UsuarioGestorDto () {
        this.email = email;
        this.senha = senha;
    }
}
