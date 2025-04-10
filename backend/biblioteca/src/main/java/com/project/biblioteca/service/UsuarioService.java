package com.project.biblioteca.service;

import java.util.List;
import java.util.Optional;

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
            usuario.setSobreNome(dto.getSobreNome());
            usuario.setDataNasc(dto.getDataNasc());
            usuario.setCPF(dto.getCpf());
            usuario.setEndereco(dto.getEndereco());
            usuario.setTelefone(dto.getTelefone());
            usuario.setMaxLivro(gestor.getMax_livros());
            usuario.setConta(gestor);
            return repositoryUser.save(usuario);
        }
        return null;
    }

    public List<Usuario> listarUsuariosPorGestor(UsuarioGestor user) {
        return repositoryUser.findByConta(user);
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
