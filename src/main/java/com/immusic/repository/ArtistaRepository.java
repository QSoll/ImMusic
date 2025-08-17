package com.immusic.repository;

import com.immusic.model.Artista;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistaRepository extends MongoRepository<Artista, String> {
    // Aqui você pode adicionar métodos personalizados, se quiser
}
