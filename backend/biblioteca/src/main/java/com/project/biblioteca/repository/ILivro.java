package com.project.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.UsuarioGestor;

public interface ILivro extends JpaRepository<Livro, Integer> {
    
    List<Livro> findByConta(UsuarioGestor gestor);

    Optional<Livro> findByIdAndConta(Integer id, UsuarioGestor conta);
}
