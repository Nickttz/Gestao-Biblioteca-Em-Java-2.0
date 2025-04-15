package com.project.biblioteca.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.BibliotecaDto;
import com.project.biblioteca.model.Biblioteca;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.service.AuthHelperService;
import com.project.biblioteca.service.BibliotecaService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class BibliotecaController {

    private final BibliotecaService bibliotecaServ;
    private final AuthHelperService authHelper;

    public BibliotecaController(BibliotecaService bibliotecaServ, AuthHelperService authHelper) {
        this.bibliotecaServ = bibliotecaServ;
        this.authHelper = authHelper;
    }

    @PostMapping("/usuarios/biblioteca")
    public ResponseEntity<?> cadastrarBiblioteca(@RequestBody BibliotecaDto bibliotecaDto, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);
        Biblioteca bib = bibliotecaServ.cadastro(bibliotecaDto, gestor);
        return ResponseEntity.ok(bib);
    }

    @PutMapping("/usuarios/biblioteca")
    public ResponseEntity<?> atualizarBiblioteca(@RequestBody BibliotecaDto dto, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);

        Biblioteca atualizada = bibliotecaServ.atualizarBiblioteca(dto, gestor);

        if (atualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(atualizada);
    }

}
