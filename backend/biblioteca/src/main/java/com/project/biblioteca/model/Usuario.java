package com.project.biblioteca.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private Integer id;

    @Column(name="nome", nullable=false, length=100)
    private String nome;

    @Column(name="sobre_nome", nullable=false, length=100)
    private String sobrenome;

    @Temporal(TemporalType.DATE)
    @Column(name="data_nasc", nullable=false)
    private LocalDate dataNascimento;

    @Column(name="cpf", nullable=false, unique=true, length=14)
    private String Cpf;

    @Column(name="max_livro", nullable=false)
    private int maxLivro;

    @Column(name="endereco", nullable=false, length=255)
    private String endereco;

    @Column(name="telefone", nullable=true, length=20)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "id_conta", nullable = false)
    private UsuarioGestor conta;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livro> livros;

    public Usuario() {}

}
