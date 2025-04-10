package com.project.biblioteca.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.LivroDto;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.service.AuthHelperService;
import com.project.biblioteca.service.LivroService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class LivroController {

    private LivroService livroService;
    private AuthHelperService authHelper;

    public LivroController(LivroService livroService, AuthHelperService authHelper) {
        this.livroService = livroService;
        this.authHelper = authHelper;
    }

    @PostMapping("/livros/cadastrar")
    public ResponseEntity<?> cadastrarLivro(@RequestBody LivroDto dto, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);
        
        Livro novoLivro = livroService.cadastrar(dto, gestor);
        return ResponseEntity.status(201).body(novoLivro);
    }

    @GetMapping("/livros/listar")
    public ResponseEntity<?> listarLivros(HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);
        
        List<Livro> livros = livroService.listarPorGestor(gestor);
        return ResponseEntity.ok(livros);
    }

    @DeleteMapping("/livros/deletar/{id}")
    public ResponseEntity<?> deletarLivro(@PathVariable Integer id, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);
        
        if (livroService.deletarLivro(gestor, id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(404).body("Livro não encontrado.");
    }
}