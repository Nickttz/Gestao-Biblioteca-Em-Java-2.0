package com.project.biblioteca.dto;

import com.project.biblioteca.model.UsuarioGestor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BibliotecaDto {
    
    private Integer id;
    private String nomeBiblioteca;
    private int maxEmprestimos;
    private int diasTolerancia;
    private UsuarioGestor conta;
}
