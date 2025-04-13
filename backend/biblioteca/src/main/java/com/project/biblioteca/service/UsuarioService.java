package com.project.biblioteca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
            usuario.setId(dto.getId());
            usuario.setNome(dto.getNome());
            usuario.setSobrenome(dto.getSobrenome());
            usuario.setDataNascimento(dto.getDataNascimento());
            usuario.setCpf(dto.getCpf());
            usuario.setEndereco(dto.getEndereco());
            usuario.setTelefone(dto.getTelefone());
            usuario.setMaxLivro(0);
            List<UsuarioGestor> gestores = new ArrayList<>();
            gestores.add(gestor);
            usuario.setContas(gestores);
            return repositoryUser.save(usuario);
        }
        return null;
    }

    public List<UsuarioDto> listarUsuariosPorGestor(UsuarioGestor user) {
        List<Usuario> usuarios = repositoryUser.findByContas(user);
        return usuarios.stream().map(usuario -> {
            UsuarioDto dto = new UsuarioDto();
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setSobrenome(usuario.getSobrenome());
            dto.setCpf(usuario.getCpf());
            dto.setEndereco(usuario.getEndereco());
            dto.setTelefone(usuario.getTelefone());
            dto.setDataNascimento(usuario.getDataNascimento());
            dto.setMaxlivro(usuario.getMaxLivro());
            return dto;
        }).collect(Collectors.toList());
    }
    
    public Boolean deletarUsuario (UsuarioGestor usuario, UUID id) {
        Optional<Usuario> userOpt = repositoryUser.findByIdAndContas(id, usuario);

        if(userOpt.isPresent()) {
            repositoryUser.delete(userOpt.get());
            return true;
        }
        return false;
    }
}
