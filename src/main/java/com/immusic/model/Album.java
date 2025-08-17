package com.immusic.model;

import java.util.List;

public class Album {
    private String titulo;
    private String artista;
    private int anoLancamento;
    private List<MusicaLink> faixas;

    // Getters e setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public List<MusicaLink> getFaixas() {
        return faixas;
    }

    public void setFaixas(List<MusicaLink> faixas) {
        this.faixas = faixas;
    }
}
