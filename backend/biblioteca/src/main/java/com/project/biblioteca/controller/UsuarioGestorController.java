package com.project.biblioteca.controller;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.BibliotecaDto;
import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.mapper.Mapper;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.security.Token;
import com.project.biblioteca.security.TokenUtil;
import com.project.biblioteca.service.UsuarioGestorService;

@RestController
@CrossOrigin("*")
public class UsuarioGestorController {
    
    private final UsuarioGestorService usuarioGestorService;
    private final TokenUtil tokenService;

    public UsuarioGestorController (UsuarioGestorService usuarioGestorService, TokenUtil tokenService) {
        this.usuarioGestorService = usuarioGestorService;
        this.tokenService = tokenService;
    }
    
    @PostMapping("/cadastro")
    public ResponseEntity<?> criarUsuarioGestor(@RequestBody UsuarioGestorDto usuario) {
        return ResponseEntity.status(201).body(usuarioGestorService.cadastrarUsuario(usuario));
    }

    @GetMapping("/usuarios/perfil/biblioteca")
    public ResponseEntity<UsuarioGestorDto> obterPerfilGestor(@RequestHeader("Authorization") String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer", "").trim();
        String cpf = tokenService.extrairCpfDoToken(token);

        Optional <UsuarioGestor> gestorOpt = usuarioGestorService.buscarPorCpf(cpf);

        if (!gestorOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UsuarioGestor gestor = gestorOpt.get();
        UsuarioGestorDto dto = Mapper.toDto(gestor);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/usuarios/perfil/cpf")
    public ResponseEntity<String> obterCpfToken(@RequestHeader("Authorization") String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer", "").trim();
        String cpf = tokenService.extrairCpfDoToken(token);
        return ResponseEntity.ok(cpf);
    }

    @PostMapping("/usuarios/perfil/biblioteca")
    public ResponseEntity<UsuarioGestor> associarBiblioteca(@RequestHeader("Authorization") String authHeader, @RequestBody BibliotecaDto dto) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    
        String token = authHeader.replace("Bearer", "").trim(); 
        String cpf = tokenService.extrairCpfDoToken(token); 
        
        UsuarioGestor gestorAtualizado = usuarioGestorService.verificarPrimeiroAcesso(cpf, dto);
        if (gestorAtualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(gestorAtualizado);
    }

    @PostMapping("/login")
    public ResponseEntity <Token> realizarLogin(@RequestBody UsuarioGestorDto usuario) {
        Token token = usuarioGestorService.gerarToken(usuario);

        if(token != null) {
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("usuarios/{cpf}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable String cpf, @RequestBody UsuarioGestorDto usuarioAtualizado) {
        Optional<UsuarioGestor> usuario = usuarioGestorService.atualizarUsuario(cpf, usuarioAtualizado);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
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