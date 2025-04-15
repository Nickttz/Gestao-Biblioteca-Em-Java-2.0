package com.project.biblioteca.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.project.biblioteca.model.Biblioteca;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmprestimoDto {
    
    private String nome;
    private String sobrenome;
    private String cpf;
    private LocalDate data_devolucao;
    private UUID idcliente;
    private UUID idlivro;
    private Integer idconta;
    private Biblioteca biblioteca;
}