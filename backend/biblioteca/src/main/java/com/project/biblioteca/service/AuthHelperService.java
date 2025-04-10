package com.project.biblioteca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IUsuarioGestor;
import com.project.biblioteca.security.TokenUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthHelperService {
    
    @Autowired
    private final IUsuarioGestor gestorRepo;

    public AuthHelperService(IUsuarioGestor gestorRepo) {
        this.gestorRepo = gestorRepo;
    }

    public UsuarioGestor validarTokenEObterGestor(HttpServletRequest request) {
        Authentication auth = TokenUtil.validate(request);

        if (auth == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado!");
        }

        String cpfGestor = auth.getName();
        UsuarioGestor gestor = gestorRepo.findByCpf(cpfGestor);

        if (gestor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestor não encontrado.");
        }

        return gestor;
    }

}
