package com.project.biblioteca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.biblioteca.model.UsuarioGestor;

@Repository
public interface IUsuarioGestor extends JpaRepository<UsuarioGestor, Integer> {

    Optional<UsuarioGestor> findByEmail(String email);

    Optional<UsuarioGestor> findByCpf(String cpfGestor);

    Optional<UsuarioGestor> deleteByCpf(String cpfGestor);

    void deleteAllByCpf(String cpf);

    boolean existsByEmailAndCpf(String email, String cpf);
    
}
