package com.project.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.BibliotecaDto;
import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.model.Biblioteca;
import com.project.biblioteca.model.Emprestimo;
import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IBiblioteca;
import com.project.biblioteca.repository.IEmprestimo;
import com.project.biblioteca.repository.ILivro;
import com.project.biblioteca.repository.IUsuario;
import com.project.biblioteca.repository.IUsuarioGestor;
import com.project.biblioteca.security.Token;
import com.project.biblioteca.security.TokenUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class UsuarioGestorService {

    private final BibliotecaService bibliotecaService;

    @Autowired
    private IUsuarioGestor repository;

    @Autowired
    private IUsuario usuarioRepository;

    @Autowired
    private ILivro livroRepository;

    @Autowired
    private IEmprestimo emprestimoRepository;

    @Autowired
    private IBiblioteca bibliotecaRepository;

    private PasswordEncoder passwordEncoder;

    public UsuarioGestorService(IUsuarioGestor repository, IBiblioteca repositoryBiblioteca, BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UsuarioGestor verificarPrimeiroAcesso(String cpf, BibliotecaDto dto) {
        Optional<UsuarioGestor> gestorOpt = repository.findByCpf(cpf);
    
        if (gestorOpt.isPresent()) {
            UsuarioGestor gestor = gestorOpt.get();
            Optional<Biblioteca> bibliotecaExistente = bibliotecaRepository.findByConta(gestor);
    
            if (bibliotecaExistente.isEmpty()) {
                Biblioteca novaBiblioteca = bibliotecaService.cadastro(dto, gestor);
                gestor.setBiblioteca(novaBiblioteca);
                return repository.save(gestor);
            }

            return gestor;
        }

        return null;
    }

    public UsuarioGestor cadastrarUsuario(UsuarioGestorDto usuarioDto) {
        
        String senhaCriptografada = this.passwordEncoder.encode(usuarioDto.getSenha());
        UsuarioGestor usuario = new UsuarioGestor();
        usuario.setNome(usuarioDto.getNome());
        usuario.setSobrenome(usuarioDto.getSobrenome());
        usuario.setEmail(usuarioDto.getEmail());
        usuario.setSenha(senhaCriptografada);
        usuario.setCpf(usuarioDto.getCpf());
        usuario.setBiblioteca(null);
        usuario.setTelefone(usuarioDto.getTelefone());

        return repository.save(usuario);
    }

    public Optional<UsuarioGestor> atualizarUsuario(String cpf, UsuarioGestorDto dto) {
        Optional<UsuarioGestor> usuarioOpt = repository.findByCpf(dto.getCpf());

        if (usuarioOpt.isPresent()) {
            UsuarioGestor usuario = usuarioOpt.get();

            if (dto.getNome() != null && !dto.getNome().isBlank()) usuario.setNome(dto.getNome());
            if (dto.getSobrenome() != null && !dto.getSobrenome().isBlank()) usuario.setSobrenome(dto.getSobrenome());
            if (dto.getEmail() != null && !dto.getEmail().isBlank()) usuario.setEmail(dto.getEmail());
            if (dto.getCpf() != null && !dto.getCpf().isBlank()) usuario.setCpf(dto.getCpf());
            if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) usuario.setTelefone(dto.getTelefone());
            if (dto.getSenha() != null && !dto.getSenha().isBlank()) usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
            if (dto.getBiblioteca().getNomeBiblioteca() != null && !dto.getBiblioteca().getNomeBiblioteca().isBlank()); usuario.getBiblioteca().setNomeBiblioteca(dto.getBiblioteca().getNomeBiblioteca());
            if (dto.getBiblioteca().getDiasTolerancia() != 0); usuario.getBiblioteca().setDiasTolerancia(dto.getBiblioteca().getDiasTolerancia());
            if (dto.getBiblioteca().getMaxEmprestimos() != 0); usuario.getBiblioteca().setMaxEmprestimos(dto.getBiblioteca().getMaxEmprestimos());

            repository.save(usuario);

            return usuarioOpt;
        }

        return null;
    }

    @Transactional
    public Boolean deletarUsuario(String cpf) {
        Optional<UsuarioGestor> optionalUsuario = repository.findByCpf(cpf);

        if (optionalUsuario.isPresent()) {
            UsuarioGestor gestor = optionalUsuario.get();

            List<Usuario> usuarios = usuarioRepository.findByContas(gestor);
            List<Livro> livros = livroRepository.findByContas(gestor);
            List<Emprestimo> emprestimos = emprestimoRepository.findByConta(gestor);

            if (!usuarios.isEmpty()) {
                usuarioRepository.deleteAll(usuarios);
                livroRepository.deleteAll(livros);
                emprestimoRepository.deleteAll(emprestimos);
            }

            repository.delete(gestor);
            return true;
        }

        return false;
    }

    public Boolean validarUsuario(UsuarioGestor usuario) {
        Optional<UsuarioGestor> usuarioBanco = repository.findById(usuario.getId());

        if (usuarioBanco.isPresent()) {
            String senha = usuarioBanco.get().getSenha();
            return passwordEncoder.matches(usuario.getSenha(), senha);
        }

        return false;
    }

    public Token gerarToken(@Valid UsuarioGestorDto usuarioDto) {
        Optional<UsuarioGestor> userOpt = repository.findByEmail(usuarioDto.getEmail());

        if (userOpt.isPresent()) {
            UsuarioGestor user = userOpt.get();

            if (passwordEncoder.matches(usuarioDto.getSenha(), user.getSenha())) {
                return new Token(TokenUtil.criarToken(user));
            }
        }

        return null;
    }

    public Optional<UsuarioGestor> buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
