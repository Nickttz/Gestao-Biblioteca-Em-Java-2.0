package com.project.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "biblioteca")
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nomeBiblioteca;

    @Column(name = "max_emprestimos_acumulados")
    private int maxEmprestimos;

    @Column(name = "dias_tolerancia_atraso")
    private int diasTolerancia;

    @OneToOne
    @JoinColumn(name = "id_gestor", referencedColumnName = "id", nullable = false, unique = true)
    @JsonIgnore
    private UsuarioGestor conta;
}