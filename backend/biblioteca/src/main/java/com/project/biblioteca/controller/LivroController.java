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

import com.project.biblioteca.dto.LivroDto;
import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.mapper.Mapper;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.service.AuthHelperService;
import com.project.biblioteca.service.LivroService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class LivroController {

    private final LivroService livroService;
    private final AuthHelperService authHelper;

    public LivroController(LivroService livroService, AuthHelperService authHelper) {
        this.livroService = livroService;
        this.authHelper = authHelper;
    }

    @PostMapping("/usuarios/livros/cadastrar_livro")
    public ResponseEntity<?> cadastrarLivro(@RequestBody LivroDto dto, HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);
        
        livroService.cadastrar(dto, gestor);
        return ResponseEntity.status(201).body("Livro cadastrado com sucesso");
    }

    @PutMapping("/usuarios/livros/atualizar_livro")
    public ResponseEntity<?> atualizarLivro(@RequestBody LivroDto dto, HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);

        livroService.atualizar(dto.getId(), dto, gestor);
        return ResponseEntity.status(201).body("Livro atualizado com sucesso");
    }

    @GetMapping("/usuarios/livros/listar_livro")
    public ResponseEntity<?> listarLivros(HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);
        
        List<LivroDto> livros = livroService.listarPorGestor(gestor);
        return ResponseEntity.ok(livros);
    }

    @DeleteMapping("/usuarios/livros/deletar_livros/{id}")
    public ResponseEntity<?> deletarLivro(@PathVariable UUID id, HttpServletRequest request) {
        UsuarioGestor gestorOpt = authHelper.validarTokenEObterGestor(request);
        UsuarioGestorDto gestor = Mapper.toDto(gestorOpt);
        
        if (livroService.deletarLivro(gestor, id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(404).body("Livro não encontrado ou possui emprestimo pendente.");
    }
}