package com.project.biblioteca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.EmprestimoDto;
import com.project.biblioteca.dto.LivroDto;
import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.model.Emprestimo;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IEmprestimo;
import com.project.biblioteca.repository.ILivro;
import com.project.biblioteca.repository.IUsuarioGestor;

import jakarta.transaction.Transactional;

@Service
public class LivroService {

    @Autowired
    private ILivro livroRepo;

    @Autowired
    private IEmprestimo emprestimoRepositoy;
    
    @Autowired
    private IUsuarioGestor repositoryGestor;

    public Livro cadastrar(LivroDto dto, UsuarioGestorDto gestorDto) {
        Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestorDto.getCpf());
        UsuarioGestor gestor = gestorOpt.get();

        if (gestor != null) {
            Livro livro = new Livro();
            livro.setTituloDoLivro(dto.getTituloDoLivro());
            livro.setCategoria(dto.getCategoria());
            livro.setQuantidade(dto.getQuantidade());
            livro.setEmprestados(0);
            livro.setBiblioteca(dto.getBiblioteca());

            List<UsuarioGestor> gestores = new ArrayList<>();
            gestores.add(gestor);
            livro.setContas(gestores);

            return livroRepo.save(livro);
        }

        return null;
    }

    public Livro atualizar(UUID id, LivroDto dto, UsuarioGestorDto gestorDto) {
        Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestorDto.getCpf());
        UsuarioGestor gestor = gestorOpt.get();

        if (gestor != null) {
            Optional<Livro> livroOpt = livroRepo.findByIdAndContas(id, gestor);
            if (livroOpt.isPresent()) {
                Livro livro = livroOpt.get();

                if (dto.getTituloDoLivro() != null && !dto.getTituloDoLivro().isBlank()) livro.setTituloDoLivro(dto.getTituloDoLivro());
                if (dto.getCategoria() != null && !dto.getCategoria().isBlank()) livro.setCategoria(dto.getCategoria());
                if (dto.getQuantidade() != null) livro.setQuantidade(dto.getQuantidade());
                if (dto.getEmprestados() != null) livro.setEmprestados(dto.getEmprestados());

                return livroRepo.save(livro);
            }
        }

        return null;
    }

    public List<LivroDto> listarPorGestor(UsuarioGestorDto gestorDto) {
        Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestorDto.getCpf());
        UsuarioGestor gestor = gestorOpt.get();

        if (gestor == null) return new ArrayList<>();

        List<Livro> livros = livroRepo.findByContas(gestor);

        return livros.stream().map(livro -> {
            LivroDto dto = new LivroDto();
            dto.setId(livro.getId());
            dto.setTituloDoLivro(livro.getTituloDoLivro());
            dto.setCategoria(livro.getCategoria());
            dto.setQuantidade(livro.getQuantidade());
            dto.setEmprestados(livro.getEmprestados());

            List<EmprestimoDto> emprestimos = new ArrayList<>();
            List<Usuario> usuarios = livro.getUsuarios();

            if (usuarios != null) {
                for (Usuario usuario : usuarios) {
                    EmprestimoDto empDto = new EmprestimoDto();
                    empDto.setNome(usuario.getNome());
                    empDto.setSobrenome(usuario.getSobrenome());
                    empDto.setCpf(usuario.getCpf());
                    emprestimos.add(empDto);
                }
            }

            dto.setClientes(emprestimos);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public boolean deletarLivro(UsuarioGestorDto gestorDto, UUID id) {
        Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestorDto.getCpf());

        if (gestorOpt != null) {
            UsuarioGestor gestor = gestorOpt.get();
            Optional<Livro> livroOpt = livroRepo.findByIdAndContas(id, gestor);
            Livro book = livroOpt.get();
            
            if (livroOpt.isPresent() && emprestimoRepositoy.existsByLivro_IdAndContaAndDataDevolucaoIsNull(id, gestor)) {
                livroRepo.delete(book);
                return true;
            }

            if(!emprestimoRepositoy.existsByLivro_IdAndContaAndDataDevolucaoIsNull(id, gestor)) {
                List<Emprestimo> emprestimos = emprestimoRepositoy.findAllByLivro_IdAndConta(id, gestor);
                emprestimoRepositoy.deleteAll(emprestimos);
                livroRepo.delete(book);
                return true;
            }
        }
        return false;
    }
}
