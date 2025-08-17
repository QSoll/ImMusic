package com.immusic.model;

import com.immusic.model.MusicaLink;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

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

    // Getters usados no controller ou frontend
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getCidadeUf() {
        return cidadeUf;
    }

    public String getBanda() {
        return banda;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public List<String> getEstilosMusicais() {
        return estilosMusicais;
    }

    public List<String> getInstrumentos() {
        return instrumentos;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGithub() {
        return github;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getKwai() {
        return kwai;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getThreads() {
        return threads;
    }

    public String getTiktok() {
        return tiktok;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public String getAmazonMusic() {
        return amazonMusic;
    }

    public String getAppleMusic() {
        return appleMusic;
    }

    public String getBandcamp() {
        return bandcamp;
    }

    public String getDeezer() {
        return deezer;
    }

    public String getPalcom3() {
        return palcom3;
    }

    public String getResso() {
        return resso;
    }

    public String getSoundcloud() {
        return soundcloud;
    }

    public String getSpotify() {
        return spotify;
    }

    public String getSuaMusica() {
        return suaMusica;
    }

    public String getTidal() {
        return tidal;
    }

    public String getYoutubeMusic() {
        return youtubeMusic;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public List<MusicaLink> getMusicasIndividuais() {
        return musicasIndividuais;
    }

    public void setMusicasIndividuais(List<MusicaLink> musicasIndividuais) {
        this.musicasIndividuais = musicasIndividuais;
    }

}