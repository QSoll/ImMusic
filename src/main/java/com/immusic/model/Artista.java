package com.immusic.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data

@Document(collection = "artistas")
public class Artista {

    @Id
    private String id;

    // Dados pessoais
    private String nome;
    private String email;
    private String whatsapp;
    private String cidadeUf;
    private String banda;
    private String especialidade;

    // Estilos e instrumentos
    private List<String> estilosMusicais;
    private List<String> instrumentos;

    // Redes sociais
    private String facebook;
    private String github;
    private String instagram;
    private String kwai;
    private String linkedin;
    private String threads;
    private String tiktok;
    private String twitter;

    // Plataformas musicais
    private String youtube;
    private String amazonMusic;
    private String appleMusic;
    private String bandcamp;
    private String deezer;
    private String palcom3;
    private String resso;
    private String soundcloud;
    private String spotify;
    private String suaMusica;
    private String tidal;
    private String youtubeMusic;

    // Músicas individuais
    private List<MusicaLink> musicasIndividuais;

    // Biografia e imagem
    private String biografia;
    private String fotoUrl;
}
