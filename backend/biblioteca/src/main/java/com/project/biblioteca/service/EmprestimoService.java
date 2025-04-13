package com.project.biblioteca.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biblioteca.enums.Status;
import com.project.biblioteca.model.Emprestimo;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IEmprestimo;
import com.project.biblioteca.repository.ILivro;
import com.project.biblioteca.repository.IUsuario;

@Service
public class EmprestimoService {

    @Autowired
    private IEmprestimo empRepo;

    @Autowired
    private IUsuario repositoryUser;

    @Autowired
    private ILivro repositoryLivro;

    public Status emprestar(Usuario userDto, Livro livroDto, UsuarioGestor user, LocalDate dataPrevistaDevolucao) {

        Optional<Emprestimo> emprestimoExistente = empRepo.findByClienteAndLivroAndContasAndDataDevolucaoIsNull(userDto, livroDto, user);

        if (emprestimoExistente.isPresent()) {
            Emprestimo emp = emprestimoExistente.get();
            emp.setDataPrevistaDevolucao(dataPrevistaDevolucao);
            empRepo.save(emp);
            return Status.RENOVACAO_SUCESSO;
        }
    
        if (livroDto.getQuantidade() <= 0) {
            return Status.LIVRO_INDISPONIVEL;
        }
    
        int emprestimoCont = empRepo.countByClienteAndLivroAndContas(userDto, livroDto, user);
        if (emprestimoCont >= user.getMax_livros()) {
            return Status.MAX_LIVROS_EXCEDIDO;
        }
    
        Emprestimo emp = new Emprestimo();
        emp.setCliente(userDto);
        emp.setLivro(livroDto);
        emp.setDataEmprestimo(LocalDate.now());
        emp.setDataPrevistaDevolucao(dataPrevistaDevolucao);
        emp.setDataDevolucao(null);
        emp.setContas(user);
    
        livroDto.setEmprestados(livroDto.getEmprestados() + 1);
        livroDto.setQuantidade(livroDto.getQuantidade() - 1);
        userDto.setMaxLivro(userDto.getMaxLivro() + 1);
    
        empRepo.save(emp);
        repositoryLivro.save(livroDto);
        repositoryUser.save(userDto);
    
        return Status.SUCESSO;
    }

    public Status devolver(Usuario userDto, Livro livroDto, UsuarioGestor user) {

  
        Optional<Emprestimo> empOpt = empRepo.findByClienteAndLivroAndContasAndDataDevolucaoIsNull(userDto, livroDto, user);

        if (empOpt.isEmpty()) return Status.EMPRESTIMO_NAO_ENCONTRADO;

        Emprestimo emp = empOpt.get(); 

        emp.setDataDevolucao(LocalDate.now());
        livroDto.setEmprestados(livroDto.getEmprestados() - 1);
        livroDto.setQuantidade(livroDto.getQuantidade() + 1);
        userDto.setMaxLivro(userDto.getMaxLivro() - 1);

        System.out.println(userDto.getMaxLivro());

        empRepo.save(emp);
        repositoryLivro.save(livroDto);
        repositoryUser.save(userDto);

        if (emp.getDataPrevistaDevolucao() != null &&
            emp.getDataDevolucao().isAfter(emp.getDataPrevistaDevolucao())) {
            return Status.LIVRO_DEVOLVIDO_COM_ATRASO;
        }

        return Status.SUCESSO;
    }
}
