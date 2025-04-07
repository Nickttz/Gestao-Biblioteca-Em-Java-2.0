package com.project.biblioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo_do_livro", nullable = false)
    private String tituloDoLivro;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;

    @Column(name = "emprestado", nullable = false)
    private int emprestados;

    @ManyToOne
    @JoinColumn(name = "id_conta", nullable = false)
    private UsuarioGestor conta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    public Livro() {}
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTituloDoLivro() {
        return this.tituloDoLivro;
    }

    public void setTituloDoLivro(String tituloDoLivro) {
        this.tituloDoLivro = tituloDoLivro;
    }
    
    public String getCategoria(){
        return this.categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getEmprestados() {
        return this.emprestados;
    }

    public void setEmprestados(int emprestados) {
        this.emprestados = emprestados;
    }
}
