package com.project.biblioteca.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @Column(name = "max_dias")
    private int max_dias;

    @Column(name = "senha", columnDefinition = "TEXT", nullable = true)
    private String senha;

    @Column(name = "max_livros")
    private int max_livros;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livro> livros;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usuario> usuarios;

    public UsuarioGestor() {}

}
