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
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.ILivro;
import com.project.biblioteca.repository.IUsuarioGestor;

@Service
public class LivroService {

    @Autowired
    private ILivro livroRepo;
    @Autowired
    private IUsuarioGestor repositoryGestor;

    public Livro cadastrar(LivroDto dto, UsuarioGestor gestor) {
        
        gestor = repositoryGestor.findByCpf(gestor.getCpf());

        if(gestor != null) {
            
            Livro livro = new Livro();
            livro.setTituloDoLivro(dto.getTituloDoLivro());
            livro.setCategoria(dto.getCategoria());
            livro.setQuantidade(dto.getQuantidade());
            livro.setEmprestados(0);
            List<UsuarioGestor> gestores = new ArrayList<>();
            gestores.add(gestor);
            livro.setContas(gestores);
            return livroRepo.save(livro);
        }
        
        return null;
    }

  public List<LivroDto> listarPorGestor(UsuarioGestor user) {
        List<Livro> livros = livroRepo.findByContas(user);

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


    public boolean deletarLivro(UsuarioGestor gestor, UUID id) {
        Optional<Livro> livroOpt = livroRepo.findByIdAndContas(id, gestor);
        
        if (livroOpt.isPresent()) {
            livroRepo.delete(livroOpt.get());
            return true;
        }

        return false;
    }
}
