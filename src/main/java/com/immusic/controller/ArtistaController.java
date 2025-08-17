package com.immusic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import java.util.List;

import com.immusic.model.Artista;
import com.immusic.repository.ArtistaRepository;

@RestController
@RequestMapping("/api/artistas")
@CrossOrigin(origins = "*")
public class ArtistaController {

    @Autowired
    private ArtistaRepository repo;

    @PostMapping
    public Artista cadastrar(@RequestBody Artista artista) {
        return repo.save(artista);
    }

    @GetMapping
    public List<Artista> listarTodos() {
        return repo.findAll();
    }

    @GetMapping("/ultimos")
    public List<Artista> listarUltimos() {
        return repo.findAll().stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId())) // Simples ordenação por ID
                .limit(6)
                .toList();
    }

    @GetMapping("/buscar")
    public Map<String, Object> buscarArtistas(@RequestParam(required = false) String nome,
            @RequestParam(required = false) String estilo) {
        List<Artista> todos = repo.findAll();

        List<Artista> filtrados = todos.stream()
                .filter(a -> (nome == null || a.getNome().toLowerCase().contains(nome.toLowerCase())) &&
                        (estilo == null || a.getEstilosMusicais().stream()
                                .anyMatch(e -> e.toLowerCase().contains(estilo.toLowerCase()))))
                .toList();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("success", true);
        resposta.put("total", filtrados.size());
        resposta.put("artistas", filtrados);

        return resposta;
    }

}
