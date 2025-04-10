package com.project.biblioteca.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;

public interface IUsuario extends JpaRepository<Usuario, Integer> {
    
    List<Usuario> findByConta(UsuarioGestor gestor);

    Optional<Usuario> findByIdAndConta(Integer id, UsuarioGestor conta);
}
