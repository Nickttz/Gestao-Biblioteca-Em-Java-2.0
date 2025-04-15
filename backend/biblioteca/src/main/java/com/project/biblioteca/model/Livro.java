package com.project.biblioteca.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "titulo_do_livro", nullable = false)
    private String tituloDoLivro;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Column(name = "emprestado", nullable = false)
    private int emprestados;

    @OneToMany(mappedBy = "livro")
    private List<Emprestimo> emprestimos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "livro_conta",
        joinColumns = @JoinColumn(name = "id_livro"),
        inverseJoinColumns = @JoinColumn(name = "id_conta")
    )
    private List<UsuarioGestor> contas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "livro_usuario",
        joinColumns = @JoinColumn(name = "id_livro"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_biblioteca")
    private Biblioteca biblioteca;

    public Livro() {}
}
