package com.project.biblioteca.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuario_gestor")
public class UsuarioGestor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private Integer id;

    @Column(name = "cpf", length = 14, nullable = true, unique = true)
    private String cpf;

    @Column(name = "nome", length = 100, nullable = true)
    private String nome;

    @Column(name = "sobrenome", length = 100, nullable = true)
    private String sobrenome;

    @Column(name = "email", columnDefinition = "TEXT", nullable = true)
    private String email;

    @Column(name = "telefone", columnDefinition = "TEXT", nullable = true)
    private String telefone;

    @Column(name = "senha", columnDefinition = "TEXT", nullable = true)
    private String senha;

    @ManyToMany(mappedBy = "contas", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Livro> livros = new ArrayList<>();

    @ManyToMany(mappedBy = "contas", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Usuario> usuarios = new ArrayList<>();

    @OneToMany(mappedBy = "conta", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Emprestimo> emprestimos = new ArrayList<>();

    @OneToOne(mappedBy = "conta", cascade = CascadeType.ALL)
    private Biblioteca biblioteca;

    public UsuarioGestor() {}

}
