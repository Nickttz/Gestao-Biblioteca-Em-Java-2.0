package com.project.biblioteca.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.biblioteca.dto.EmprestimoDto;
import com.project.biblioteca.enums.Status;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.ILivro;
import com.project.biblioteca.repository.IUsuario;
import com.project.biblioteca.service.AuthHelperService;
import com.project.biblioteca.service.EmprestimoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("*")
public class EmprestimoController {

    private EmprestimoService empServ;
    private AuthHelperService authHelper;
    @Autowired
    private IUsuario repositoryUser;
    @Autowired
    private ILivro repositoryLivro;

    public EmprestimoController(EmprestimoService empServ, AuthHelperService authHelper) {
        this.empServ = empServ;
        this.authHelper = authHelper;
    }
    
    @PostMapping("/usuarios/emprestimos/realizar_emprestimo")
    public ResponseEntity<?> realizarEmprestimo(@RequestBody EmprestimoDto empDto, HttpServletRequest request) {

        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);

        Optional<Usuario> clienteOpt = repositoryUser.findByIdAndContas(empDto.getIdcliente(), gestor);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        Optional<Livro> livroOpt = repositoryLivro.findByIdAndContas(empDto.getIdlivro(), gestor);
        if (livroOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Livro não encontrado.");
        }

        Usuario cliente = clienteOpt.get();
        Livro livro = livroOpt.get();

        if (cliente.getMaxLivro() >= gestor.getMax_livros()) {
            return ResponseEntity.status(400).body("Limite de livros emprestados atingido.");
        }

        Status status = empServ.emprestar(cliente, livro, gestor, empDto.getData_devolucao());
        
        switch (status) {
            case SUCESSO:
                return ResponseEntity.ok("Empréstimo realizado com sucesso!");

            case RENOVACAO_SUCESSO:
                return ResponseEntity.ok("Renovação realizada com sucesso!");

            case MAX_LIVROS_EXCEDIDO:
                return ResponseEntity.status(400).body("Limite de livros emprestados atingido.");

            case LIVRO_INDISPONIVEL:
                return ResponseEntity.status(409).body("Livro indisponível.");

            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro");
        }
    }

    @PostMapping("/usuarios/emprestimos/realizar_devolucao")
    public ResponseEntity<?> realizarDevolucao(@RequestBody EmprestimoDto empDto, HttpServletRequest request) {
        UsuarioGestor gestor = authHelper.validarTokenEObterGestor(request);

        Optional<Usuario> clienteOpt = repositoryUser.findByIdAndContas(empDto.getIdcliente(), gestor);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }
    
        Optional<Livro> livroOpt = repositoryLivro.findByIdAndContas(empDto.getIdlivro(), gestor);
        if (livroOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Livro não encontrado.");
        }

        Usuario cliente = clienteOpt.get();
        Livro livro = livroOpt.get();

        Status status = empServ.devolver(cliente, livro, gestor);

        switch (status) {
            case SUCESSO:
                return ResponseEntity.ok("Devolução realizado com sucesso!");
    
            case MAX_LIVROS_EXCEDIDO:
                return ResponseEntity.status(400).body("Limite de livros emprestados atingido.");
    
            case LIVRO_INDISPONIVEL:
                return ResponseEntity.status(409).body("Livro indisponível.");
    
            default:
                return ResponseEntity.status(500).body("Erro inesperado.");
        }
    }
}