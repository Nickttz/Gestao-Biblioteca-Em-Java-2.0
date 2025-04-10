package com.project.biblioteca.dto;

import java.util.GregorianCalendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {

    private String nome;
    private String sobreNome;
    private GregorianCalendar dataNasc;
    private String cpf;
    private String endereco;
    private String telefone;

}