package com.project.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.LivroDto;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.ILivro;

@Service
public class LivroService {

    @Autowired
    private ILivro livroRepo;

    public Livro cadastrar(LivroDto dto, UsuarioGestor gestor) {
        Livro livro = new Livro();
        livro.setTituloDoLivro(dto.getTituloDoLivro());
        livro.setCategoria(dto.getCategoria());
        livro.setQuantidade(dto.getQuantidade());
        livro.setEmprestados(0);
        livro.setConta(gestor);

        return livroRepo.save(livro);
    }

    public List<Livro> listarPorGestor(UsuarioGestor gestor) {
        return livroRepo.findByConta(gestor);
    }

    public boolean deletarLivro(UsuarioGestor gestor, Integer id) {
        Optional<Livro> livroOpt = livroRepo.findByIdAndConta(id, gestor);
        
        if (livroOpt.isPresent()) {
            livroRepo.delete(livroOpt.get());
            return true;
        }

        return false;
    }
}
