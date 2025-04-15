package com.project.biblioteca.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.UsuarioDto;
import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.mapper.Mapper;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.service.AuthHelperService;
import com.project.biblioteca.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class UsuarioController {
    
    private final UsuarioService userServ;
    private final AuthHelperService authHelper;

    public UsuarioController(UsuarioService userServ, AuthHelperService authHelper) {
        this.userServ = userServ;
        this.authHelper = authHelper;
    }

    @PostMapping("/usuarios/clientes/cadastrar_cliente")
    public ResponseEntity<?> cadastroCliente(@RequestBody UsuarioDto dtoUser, HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);

        userServ.cadastrar(dtoUser, gestor);
        return ResponseEntity.status(201).body("Cliente cadastrado com sucesso!");
    }

    @PutMapping("/usuarios/clientes/atualizar_dados")
    public ResponseEntity<?> atualizarLivro(@RequestBody UsuarioDto dto, HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);

        userServ.atualizarCliente(dto.getId(), dto, gestor);
        return ResponseEntity.status(201).body("Livro atualizado com sucesso");
    }

    @GetMapping("/usuarios/clientes/listar_cliente")
    public ResponseEntity<?> listarCliente(HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);

        List<UsuarioDto> listaUsuario = userServ.listarUsuariosPorGestor(gestor);
        return ResponseEntity.ok(listaUsuario);

    }

    @DeleteMapping("/usuarios/clientes/deletar_cliente/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable UUID id, HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);

        if (userServ.deletarUsuario(gestor, id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(404).body("Cliente não encontrado.");
    }
}   
