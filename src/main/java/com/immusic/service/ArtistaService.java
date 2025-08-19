package com.immusic.service;

import com.immusic.model.Artista;
import com.immusic.repository.ArtistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    // Você pode adicionar mais métodos aqui depois
}
