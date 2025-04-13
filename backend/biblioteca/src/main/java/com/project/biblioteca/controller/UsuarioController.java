package com.project.biblioteca.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.UsuarioDto;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.service.AuthHelperService;
import com.project.biblioteca.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class UsuarioController {
    
    private UsuarioService userServ;
    private AuthHelperService authHelper;

    public UsuarioController(UsuarioService userServ, AuthHelperService authHelper) {
        this.userServ = userServ;
        this.authHelper = authHelper;
    }

    @PostMapping("/usuarios/clientes/cadastrar_cliente")
    public ResponseEntity<?> cadastroCliente(@RequestBody UsuarioDto dtoUser, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);

        userServ.cadastrar(dtoUser, gestor);
        return ResponseEntity.status(201).body("Cliente cadastrado com sucesso!");
    }

    @GetMapping("/usuarios/clientes/listar_cliente")
    public ResponseEntity<?> listarCliente(HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);

        List<UsuarioDto> listaUsuario = userServ.listarUsuariosPorGestor(gestor);
        return ResponseEntity.ok(listaUsuario);

    }

    @DeleteMapping("/usuarios/clientes/deletar_cliente/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable UUID id, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);

        if (userServ.deletarUsuario(gestor, id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(404).body("Cliente não encontrado.");
    }
}   
