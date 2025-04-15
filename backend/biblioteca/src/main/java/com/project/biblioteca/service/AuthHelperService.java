package com.project.biblioteca.service;


import java.util.Optional;

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
    private IUsuarioGestor gestorRepo;

    public AuthHelperService(IUsuarioGestor gestorRepo) {
        this.gestorRepo = gestorRepo;
    }

    public UsuarioGestor validarTokenEObterGestor(HttpServletRequest request) {
        Authentication auth = TokenUtil.validate(request);

        if (auth == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado!");
        }

        String cpfGestor = auth.getName();
        Optional<UsuarioGestor> gestorOpt = gestorRepo.findByCpf(cpfGestor);
        UsuarioGestor gestor = gestorOpt.get();

        if (!gestorOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gestor não encontrado.");
        }

        return gestor;
    }

}
