package com.project.biblioteca.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.UsuarioGestor;

public interface ILivro extends JpaRepository<Livro, UUID> {
    
    List<Livro> findByContas(UsuarioGestor gestor);

    Optional<Livro> findByIdAndContas(UUID id, UsuarioGestor conta);
}
