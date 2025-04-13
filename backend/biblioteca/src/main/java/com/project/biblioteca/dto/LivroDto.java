package com.project.biblioteca.dto;

import java.util.List;
import java.util.UUID;

import com.project.biblioteca.model.UsuarioGestor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LivroDto {
    private UUID id;
    private String tituloDoLivro;
    private String categoria;
    private int quantidade;
    private int emprestados;
    private List<EmprestimoDto> clientes;
    private List<UsuarioGestor> contas;
}