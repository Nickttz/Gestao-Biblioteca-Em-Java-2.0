package com.project.biblioteca.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.biblioteca.model.Emprestimo;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;

public interface IEmprestimo extends JpaRepository<Emprestimo, UUID> {

    Optional<Emprestimo> findByClienteAndLivroAndContaAndDataDevolucaoIsNull(Usuario cliente, Livro livro, UsuarioGestor contas);

    int countByClienteAndLivroAndConta(Usuario cliente, Livro livro, UsuarioGestor contas);

    List<Emprestimo> findByConta(UsuarioGestor gestor);

    boolean existsByLivro_IdAndContaAndDataDevolucaoIsNull(UUID id, UsuarioGestor gestor);

    boolean existsByCliente_IdAndContaAndDataDevolucaoIsNull(UUID id, UsuarioGestor gestor);

    List<Emprestimo> findAllByLivro_IdAndConta(UUID id, UsuarioGestor gestor);

    List<Emprestimo> findAllByCliente_IdAndConta(UUID id, UsuarioGestor gestor);
}
