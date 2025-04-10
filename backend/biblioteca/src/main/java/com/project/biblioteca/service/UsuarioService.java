package com.project.biblioteca.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.UsuarioDto;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IUsuario;
import com.project.biblioteca.repository.IUsuarioGestor;

@Service
public class UsuarioService {
    
    @Autowired
    private IUsuario repositoryUser;
    @Autowired
    private IUsuarioGestor repositoryGestor;

    public Usuario cadastrar(UsuarioDto dto, UsuarioGestor gestor) {
        
        gestor = repositoryGestor.findByCpf(gestor.getCpf());

        if(gestor != null) {
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setSobrenome(dto.getSobrenome());
            usuario.setDataNascimento(dto.getDataNascimento());
            usuario.setCpf(dto.getCpf());
            usuario.setEndereco(dto.getEndereco());
            usuario.setTelefone(dto.getTelefone());
            usuario.setMaxLivro(gestor.getMax_livros());
            usuario.setConta(gestor);
            return repositoryUser.save(usuario);
        }
        return null;
    }

    public List<UsuarioDto> listarUsuariosPorGestor(UsuarioGestor user) {
        List<Usuario> usuarios = repositoryUser.findByConta(user);

        return usuarios.stream().map(usuario -> {
            UsuarioDto dto = new UsuarioDto();
            dto.setNome(usuario.getNome());
            dto.setSobrenome(usuario.getSobrenome());
            dto.setCpf(usuario.getCpf());
            dto.setEndereco(usuario.getEndereco());
            dto.setTelefone(usuario.getTelefone());
            dto.setDataNascimento(usuario.getDataNascimento());
            return dto;
        }).collect(Collectors.toList());
    }
    
    public Boolean deletarUsuario (UsuarioGestor usuario, Integer id) {
        Optional<Usuario> userOpt = repositoryUser.findByIdAndConta(id, usuario);

        if(userOpt.isPresent()) {
            repositoryUser.delete(userOpt.get());
            return true;
        }
        return false;
    }
}
