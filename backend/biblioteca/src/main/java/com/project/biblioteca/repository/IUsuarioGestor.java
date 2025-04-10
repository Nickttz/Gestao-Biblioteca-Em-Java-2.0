package com.project.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.biblioteca.model.UsuarioGestor;

@Repository
public interface IUsuarioGestor extends JpaRepository<UsuarioGestor, Integer> {

    UsuarioGestor findByEmail(String email);

    UsuarioGestor findByCpf(String cpfGestor);

}
