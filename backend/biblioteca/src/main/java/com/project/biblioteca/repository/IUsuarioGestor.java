package com.project.biblioteca.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.project.biblioteca.model.UsuarioGestor;

@Repository
public interface IUsuarioGestor extends CrudRepository<UsuarioGestor, Integer> {

}
