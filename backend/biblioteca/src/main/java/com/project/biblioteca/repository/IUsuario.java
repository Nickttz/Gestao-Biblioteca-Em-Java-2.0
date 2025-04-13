package com.project.biblioteca.repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;

public interface IUsuario extends JpaRepository<Usuario, UUID> {
    
    List<Usuario> findByContas(UsuarioGestor gestor);

    Optional<Usuario> findByIdAndContas(UUID id, UsuarioGestor conta);
}
