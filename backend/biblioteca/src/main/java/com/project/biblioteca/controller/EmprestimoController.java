package com.project.biblioteca.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.EmprestimoDto;
import com.project.biblioteca.enums.Status;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.service.AuthHelperService;
import com.project.biblioteca.service.EmprestimoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class EmprestimoController {

    private final EmprestimoService empServ;
    private final AuthHelperService authHelper;

    public EmprestimoController(EmprestimoService empServ, AuthHelperService authHelper) {
        this.empServ = empServ;
        this.authHelper = authHelper;
    }

    @PostMapping("/usuarios/emprestimos/realizar_emprestimo")
    public ResponseEntity<?> realizarEmprestimo(@RequestBody EmprestimoDto empDto, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);
        Status status = empServ.emprestarPorDto(empDto, gestor);

        return switch (status) {
            case SUCESSO -> ResponseEntity.ok("Empréstimo realizado com sucesso!");
            case RENOVACAO_SUCESSO -> ResponseEntity.ok("Renovação realizada com sucesso!");
            case MAX_LIVROS_EXCEDIDO -> ResponseEntity.status(400).body("Limite de livros emprestados atingido.");
            case LIVRO_INDISPONIVEL -> ResponseEntity.status(409).body("Livro indisponível.");
            case CLIENTE_NAO_ENCONTRADO -> ResponseEntity.status(404).body("Usuário não encontrado.");
            case LIVRO_NAO_ENCONTRADO -> ResponseEntity.status(404).body("Livro não encontrado.");
            case BIBLIOTECA_NAO_ENCONTRADA -> ResponseEntity.status(404).body("Biblioteca não encontrada.");
            default -> ResponseEntity.status(500).body("Erro inesperado.");
        };
    }

    @PostMapping("/usuarios/emprestimos/realizar_devolucao")
    public ResponseEntity<?> realizarDevolucao(@RequestBody EmprestimoDto empDto, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);
        Status status = empServ.devolverPorDto(empDto, gestor);

        return switch (status) {
            case SUCESSO -> ResponseEntity.ok("Devolução realizada com sucesso!");
            case LIVRO_DEVOLVIDO_COM_ATRASO -> ResponseEntity.ok("Livro devolvido com atraso.");
            case EMPRESTIMO_NAO_ENCONTRADO -> ResponseEntity.status(404).body("Empréstimo não encontrado.");
            case CLIENTE_NAO_ENCONTRADO -> ResponseEntity.status(404).body("Usuário não encontrado.");
            case LIVRO_NAO_ENCONTRADO -> ResponseEntity.status(404).body("Livro não encontrado.");
            case BIBLIOTECA_NAO_ENCONTRADA -> ResponseEntity.status(404).body("Biblioteca não encontrada.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado.");
        };
    }
}
