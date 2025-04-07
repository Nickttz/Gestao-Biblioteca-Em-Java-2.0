package com.project.biblioteca.model;

import java.util.GregorianCalendar;
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
    private String sobreNome;

    @Temporal(TemporalType.DATE)
    @Column(name="data_nasc", nullable=false)
    private GregorianCalendar DataNasc;

    @Column(name="cpf", nullable=false, unique=true, length=14)
    private String CPF;

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

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public GregorianCalendar getDataNasc() {
        return DataNasc;
    }

    public void setDataNasc(GregorianCalendar dataNasc) {
        DataNasc = dataNasc;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String cPF) {
        CPF = cPF;
    }

    public int getMaxLivro() {
        return maxLivro;
    }

    public void setMaxLivro(int maxLivro) {
        this.maxLivro = maxLivro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public UsuarioGestor getConta() {
        return conta;
    }

    public void setConta(UsuarioGestor conta) {
        this.conta = conta;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }
}
