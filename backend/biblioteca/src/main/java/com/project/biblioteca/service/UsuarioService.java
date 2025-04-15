package com.project.biblioteca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.UsuarioDto;
import com.project.biblioteca.dto.UsuarioGestorDto;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IUsuario;
import com.project.biblioteca.repository.IUsuarioGestor;

@Service
public class UsuarioService {

    @Autowired
    private IUsuario repositoryUser;
    @Autowired
    private IUsuarioGestor repositoryGestor;

    public Usuario cadastrar(UsuarioDto dto, UsuarioGestorDto gestorDto) {
        Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestorDto.getCpf());
        UsuarioGestor gestor = gestorOpt.get();

        if (gestor != null) {
            Usuario usuario = new Usuario();
            usuario.setId(dto.getId());
            usuario.setNome(dto.getNome());
            usuario.setSobrenome(dto.getSobrenome());
            usuario.setDataNascimento(dto.getDataNascimento());
            usuario.setCpf(dto.getCpf());
            usuario.setEndereco(dto.getEndereco());
            usuario.setTelefone(dto.getTelefone());
            usuario.setMaxLivro(0);
            usuario.setBiblioteca(dto.getBiblioteca());

            List<UsuarioGestor> gestores = new ArrayList<>();
            gestores.add(gestor);
            usuario.setContas(gestores);

            return repositoryUser.save(usuario);
        }
        return null;
    }

    public Usuario atualizarCliente(UUID id, UsuarioDto dto, UsuarioGestorDto gestorDto) {
        Optional<UsuarioGestor> gestor = repositoryGestor.findByCpf(gestorDto.getCpf());

        if (gestor != null) {
            Optional<Usuario> usuarioOpt = repositoryUser.findById(id);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                if (dto.getNome() != null && !dto.getNome().isBlank()) usuario.setNome(dto.getNome());
                if (dto.getSobrenome() != null && !dto.getSobrenome().isBlank()) usuario.setSobrenome(dto.getSobrenome());
                if (dto.getDataNascimento() != null) usuario.setDataNascimento(dto.getDataNascimento());
                if (dto.getCpf() != null && !dto.getCpf().isBlank()) usuario.setCpf(dto.getCpf());
                if (dto.getEndereco() != null && !dto.getEndereco().isBlank()) usuario.setEndereco(dto.getEndereco());
                if (dto.getTelefone() != null && !dto.getTelefone().isBlank()) usuario.setTelefone(dto.getTelefone());

                return repositoryUser.save(usuario);
            }
        }
        return null;
    }

    public List<UsuarioDto> listarUsuariosPorGestor(UsuarioGestorDto gestorDto) {
        Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestorDto.getCpf());
        UsuarioGestor gestor = gestorOpt.get();

        if (gestor == null) return new ArrayList<>();

        List<Usuario> usuarios = repositoryUser.findByContas(gestor);

        return usuarios.stream().map(usuario -> {
            UsuarioDto dto = new UsuarioDto();
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setSobrenome(usuario.getSobrenome());
            dto.setCpf(usuario.getCpf());
            dto.setEndereco(usuario.getEndereco());
            dto.setTelefone(usuario.getTelefone());
            dto.setDataNascimento(usuario.getDataNascimento());
            dto.setMaxlivro(usuario.getMaxLivro());
            return dto;
        }).collect(Collectors.toList());
    }

    public Boolean deletarUsuario(UsuarioGestorDto gestorDto, UUID id) {
        Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestorDto.getCpf());
        UsuarioGestor gestor = gestorOpt.get();

        if (gestor != null) {
            Optional<Usuario> userOpt = repositoryUser.findByIdAndContas(id, gestor);
            if (userOpt.isPresent()) {
                repositoryUser.delete(userOpt.get());
                return true;
            }
        }
        return false;
    }
}