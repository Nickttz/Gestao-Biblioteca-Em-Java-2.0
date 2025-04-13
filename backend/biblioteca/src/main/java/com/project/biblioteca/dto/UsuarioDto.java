package com.project.biblioteca.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.biblioteca.model.UsuarioGestor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {

    private UUID id;
    private String nome;
    private String sobrenome;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    private String cpf;
    private String endereco;
    private String telefone;
    private List<UsuarioGestor> contas;
    private int maxlivro;

}