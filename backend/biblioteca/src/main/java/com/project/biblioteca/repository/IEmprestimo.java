package com.project.biblioteca.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.biblioteca.model.Emprestimo;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;

public interface IEmprestimo extends JpaRepository<Emprestimo, UUID> {

    Optional<Emprestimo> findByClienteAndLivroAndContasAndDataDevolucaoIsNull(Usuario cliente, Livro livro, UsuarioGestor contas);

    int countByClienteAndLivroAndContas(Usuario cliente, Livro livro, UsuarioGestor contas);

}
