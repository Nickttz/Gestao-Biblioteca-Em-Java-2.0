package com.project.biblioteca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.security.Token;
import com.project.biblioteca.service.UsuarioGestorService;

@RestController
@CrossOrigin("*")
public class UsuarioGestorController {
    
    private UsuarioGestorService usuarioGestorService;

    public UsuarioGestorController (UsuarioGestorService usuarioGestorService) {
        this.usuarioGestorService = usuarioGestorService;
    }

    @GetMapping("/usuarios") 
    public ResponseEntity<List<UsuarioGestor>> listarUsuario() {
        return ResponseEntity.status(200).body(usuarioGestorService.listarUsuarios());
    }
    
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioGestor> criarUsuarioGestor(@RequestBody UsuarioGestor usuario) {
        return ResponseEntity.status(201).body(usuarioGestorService.cadastrarUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity <Token> realizarLogin(@RequestBody UsuarioGestorDto usuario) {
        Token token = usuarioGestorService.gerarToken(usuario);

        if(token != null) {
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("usuarios/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioGestor usuarioAtualizado) {
        Optional<UsuarioGestor> usuario = usuarioGestorService.atualizarUsuario(id, usuarioAtualizado);

        if (usuario.isPresent()) {
            return ResponseEntity.status(201).body(usuario.get());
        } else {
            return ResponseEntity.status(404).build();
        }
    }
    
    @DeleteMapping("usuarios/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Integer id) {
        boolean deletado = usuarioGestorService.deletarUsuario(id);
    
        if (deletado) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(404).build();
        }
    }
}