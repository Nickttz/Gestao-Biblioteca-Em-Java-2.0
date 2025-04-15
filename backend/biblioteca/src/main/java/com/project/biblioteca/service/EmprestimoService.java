package com.project.biblioteca.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.EmprestimoDto;
import com.project.biblioteca.enums.Status;
import com.project.biblioteca.model.Biblioteca;
import com.project.biblioteca.model.Emprestimo;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IBiblioteca;
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

    @Autowired
    private IBiblioteca repositoryBio;

    public Status emprestarPorDto(EmprestimoDto empDto, UsuarioGestor gestor) {
        Optional<Usuario> clienteOpt = repositoryUser.findByIdAndContas(empDto.getIdcliente(), gestor);
        if (clienteOpt.isEmpty()) return Status.CLIENTE_NAO_ENCONTRADO;

        Optional<Livro> livroOpt = repositoryLivro.findByIdAndContas(empDto.getIdlivro(), gestor);
        if (livroOpt.isEmpty()) return Status.LIVRO_NAO_ENCONTRADO;

        Optional<Biblioteca> bibliotecaOpt = repositoryBio.findByConta(gestor);
        if (bibliotecaOpt.isEmpty()) return Status.BIBLIOTECA_NAO_ENCONTRADA;

        return emprestar(bibliotecaOpt.get(), clienteOpt.get(), livroOpt.get(), gestor, empDto.getData_devolucao());
    }

    public Status devolverPorDto(EmprestimoDto empDto, UsuarioGestor gestor) {
        Optional<Usuario> clienteOpt = repositoryUser.findByIdAndContas(empDto.getIdcliente(), gestor);
        if (clienteOpt.isEmpty()) return Status.CLIENTE_NAO_ENCONTRADO;

        Optional<Livro> livroOpt = repositoryLivro.findByIdAndContas(empDto.getIdlivro(), gestor);
        if (livroOpt.isEmpty()) return Status.LIVRO_NAO_ENCONTRADO;

        Optional<Biblioteca> bibliotecaOpt = repositoryBio.findByConta(gestor);
        if (bibliotecaOpt.isEmpty()) return Status.BIBLIOTECA_NAO_ENCONTRADA;

        return devolver(bibliotecaOpt.get(), clienteOpt.get(), livroOpt.get(), gestor);
    }

    public Status emprestar(Biblioteca biblioteca, Usuario usuario, Livro livro, UsuarioGestor gestor, LocalDate dataPrevistaDevolucao) {

        Optional<Emprestimo> emprestimoExistente = empRepo.findByClienteAndLivroAndContaAndDataDevolucaoIsNull(usuario, livro, gestor);

        if (emprestimoExistente.isPresent()) {
            Emprestimo emp = emprestimoExistente.get();
            emp.setDataPrevistaDevolucao(dataPrevistaDevolucao);
            empRepo.save(emp);
            return Status.RENOVACAO_SUCESSO;
        }

        if (livro.getQuantidade() <= 0) {
            return Status.LIVRO_INDISPONIVEL;
        }

        int emprestimoCont = empRepo.countByClienteAndLivroAndConta(usuario, livro, gestor);
        if (emprestimoCont >= biblioteca.getMaxEmprestimos()) {
            return Status.MAX_LIVROS_EXCEDIDO;
        }

        Emprestimo emp = new Emprestimo();
        emp.setCliente(usuario);
        emp.setLivro(livro);
        emp.setConta(gestor);
        emp.setBiblioteca(biblioteca);
        emp.setDataEmprestimo(LocalDate.now());
        emp.setDataPrevistaDevolucao(dataPrevistaDevolucao);
        emp.setDataDevolucao(null);

        livro.setEmprestados(livro.getEmprestados() + 1);
        livro.setQuantidade(livro.getQuantidade() - 1);
        usuario.setMaxLivro(usuario.getMaxLivro() + 1);

        empRepo.save(emp);
        repositoryLivro.save(livro);
        repositoryUser.save(usuario);

        return Status.SUCESSO;
    }

    public Status devolver(Biblioteca biblioteca, Usuario usuario, Livro livro, UsuarioGestor gestor) {

        Optional<Emprestimo> empOpt = empRepo.findByClienteAndLivroAndContaAndDataDevolucaoIsNull(usuario, livro, gestor);
        if (empOpt.isEmpty()) return Status.EMPRESTIMO_NAO_ENCONTRADO;

        Emprestimo emp = empOpt.get();

        emp.setDataDevolucao(LocalDate.now());

        livro.setEmprestados(livro.getEmprestados() - 1);
        livro.setQuantidade(livro.getQuantidade() + 1);
        usuario.setMaxLivro(usuario.getMaxLivro() - 1);

        empRepo.save(emp);
        repositoryLivro.save(livro);
        repositoryUser.save(usuario);

        if (emp.getDataPrevistaDevolucao() != null &&
            emp.getDataDevolucao().isAfter(emp.getDataPrevistaDevolucao().plusDays(biblioteca.getDiasTolerancia()))) {
            return Status.LIVRO_DEVOLVIDO_COM_ATRASO;
        }

        return Status.SUCESSO;
    }
}
