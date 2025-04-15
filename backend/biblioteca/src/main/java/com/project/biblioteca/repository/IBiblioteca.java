package com.project.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.biblioteca.model.Biblioteca;
import com.project.biblioteca.model.UsuarioGestor;

public interface IBiblioteca extends JpaRepository<Biblioteca, Integer> {

    Optional<Biblioteca> findByConta(UsuarioGestor conta);

}
