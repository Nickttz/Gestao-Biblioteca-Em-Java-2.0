package com.project.biblioteca.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.biblioteca.dto.BibliotecaDto;
import com.project.biblioteca.model.Biblioteca;
import com.project.biblioteca.model.UsuarioGestor;
import com.project.biblioteca.repository.IBiblioteca;
import com.project.biblioteca.repository.IUsuarioGestor;

@Service
public class BibliotecaService {

    @Autowired
    IBiblioteca repositoryBiblioteca;

    @Autowired
    IUsuarioGestor repositoryGestor;

    public Biblioteca cadastro(BibliotecaDto bibliotecaDto, UsuarioGestor gestor) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNomeBiblioteca(bibliotecaDto.getNomeBiblioteca());
        biblioteca.setDiasTolerancia(bibliotecaDto.getDiasTolerancia());
        biblioteca.setConta(gestor);
        biblioteca.setMaxEmprestimos(bibliotecaDto.getMaxEmprestimos());
        
        return repositoryBiblioteca.save(biblioteca);
    }

    public Biblioteca atualizarBiblioteca(BibliotecaDto dto, UsuarioGestor gestor) {
    Optional<UsuarioGestor> gestorOpt = repositoryGestor.findByCpf(gestor.getCpf());

    if (gestorOpt.isPresent()) {
        Optional<Biblioteca> bibOpt = repositoryBiblioteca.findByConta(gestorOpt.get());

        if (bibOpt.isPresent()) {
            Biblioteca biblioteca = bibOpt.get();

            if (dto.getNomeBiblioteca() != null && !dto.getNomeBiblioteca().isBlank()) {
                biblioteca.setNomeBiblioteca(dto.getNomeBiblioteca());
            }
            if (dto.getMaxEmprestimos() > 0) {
                biblioteca.setMaxEmprestimos(dto.getMaxEmprestimos());
            }
            if (dto.getDiasTolerancia() > 0) {
                biblioteca.setDiasTolerancia(dto.getDiasTolerancia());
            }

            return repositoryBiblioteca.save(biblioteca);
        }
    }

    return null;
}

}
