package com.project.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IUsuarioGestor;
import com.project.biblioteca.security.Token;
import com.project.biblioteca.security.TokenUtil;

import jakarta.validation.Valid;

@Service
public class UsuarioGestorService {
    
    @Autowired
    private IUsuarioGestor repository;

    private PasswordEncoder passwordEncoder;

    public UsuarioGestorService (IUsuarioGestor repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Teste //
    public List<UsuarioGestor> listarUsuarios() {
        List<UsuarioGestor> lista = repository.findAll();
        return lista;
    }

    public UsuarioGestor cadastrarUsuario(UsuarioGestor usuario) {
        String encoder = this.passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(encoder);
        UsuarioGestor usuarioNovo = repository.save(usuario);
        return usuarioNovo;
    }

    public Optional<UsuarioGestor> atualizarUsuario(Integer id, UsuarioGestor usuarioAtualizado) {
        Optional<UsuarioGestor> optionalUsuario = repository.findById(id);

        if (optionalUsuario.isPresent()) {
            UsuarioGestor usuarioExistente = optionalUsuario.get();
            usuarioExistente.setNome(usuarioAtualizado.getNome());
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
            usuarioExistente.setSenha(this.passwordEncoder.encode(usuarioAtualizado.getSenha()));
            repository.save(usuarioExistente);
            return Optional.of(usuarioExistente);
        }

        return Optional.empty();
    }

    public Boolean deletarUsuario (Integer id) {
        Optional<UsuarioGestor> optionalUsuario = repository.findById(id);
    
        if (optionalUsuario.isPresent()) {
            repository.deleteById(id);
            return true;
        }

        return false;
    }

    public Boolean validarUsuario (UsuarioGestor usuario) {
        String senha = repository.getReferenceById(usuario.getId()).getSenha();
        
        if (passwordEncoder.matches(usuario.getSenha(), senha)) {
            return true;
        }
        
        return false;
    }

    public Token gerarToken(@Valid UsuarioGestorDto usuario) {
        UsuarioGestor user = repository.findByEmail(usuario.getEmail());

        if(user != null && passwordEncoder.matches(usuario.getSenha(), user.getSenha())) {
            return new Token(TokenUtil.criarToken(user));
        }
        return null;
    }
}
