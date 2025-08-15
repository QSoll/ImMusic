package com.musicosapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Modelo que representa um artista na plataforma I'MUSIC
 */
@Entity
@Table(name = "artistas")
public class Artista {

    @Id
    private String id;

    @Column(nullable = false)
    private String nome;

    private String estilo;
    private String especialidade;
    private String link;
    private String biografia;
    private String fotoUrl;
    private LocalDateTime dataCadastro;

    // Construtor padrão
    public Artista() {
        this.dataCadastro = LocalDateTime.now();
        this.id = generateId();
    }

    // Construtor com parâmetros principais
    public Artista(String nome, String estilo, String especialidade) {
        this();
        this.nome = nome;
        this.estilo = estilo;
        this.especialidade = especialidade;
    }

    // Construtor completo
    public Artista(String nome, String estilo, String especialidade,
                   String link, String biografia, String fotoUrl) {
        this(nome, estilo, especialidade);
        this.link = link;
        this.biografia = biografia;
        this.fotoUrl = fotoUrl;
    }

    // Gera um ID único baseado no timestamp
    private String generateId() {
        return "ART_" + System.currentTimeMillis();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataCadastroFormatada() {
        return dataCadastro.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty() &&
               estilo != null && !estilo.trim().isEmpty() &&
               especialidade != null && !especialidade.trim().isEmpty();
    }

    public String[] toStringArray() {
        return new String[] {
            id != null ? id : "",
            nome != null ? nome : "",
            estilo != null ? estilo : "",
            especialidade != null ? especialidade : "",
            link != null ? link : "",
            biografia != null ? biografia : "",
            fotoUrl != null ? fotoUrl : "",
            getDataCadastroFormatada()
        };
    }

    public static Artista fromStringArray(String[] data) {
        if (data == null || data.length < 4) return null;

        Artista artista = new Artista();
        artista.setId(data.length > 0 && !data[0].isEmpty() ? data[0] : artista.getId());
        artista.setNome(data.length > 1 ? data[1] : "");
        artista.setEstilo(data.length > 2 ? data[2] : "");
        artista.setEspecialidade(data.length > 3 ? data[3] : "");
        artista.setLink(data.length > 4 ? data[4] : "");
        artista.setBiografia(data.length > 5 ? data[5] : "");
        artista.setFotoUrl(data.length > 6 ? data[6] : "");

        if (data.length > 7 && !data[7].isEmpty()) {
            try {
                artista.setDataCadastro(
                    LocalDateTime.parse(data[7], DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                );
            } catch (Exception ignored) {}
        }

        return artista;
    }

    @Override
    public String toString() {
        return "Artista{" +
               "id='" + id + '\'' +
               ", nome='" + nome + '\'' +
               ", estilo='" + estilo + '\'' +
               ", especialidade='" + especialidade + '\'' +
               ", link='" + link + '\'' +
               ", biografia='" + (biografia != null ? biografia.substring(0, Math.min(biografia.length(), 50)) + "..." : "N/A") + '\'' +
               ", fotoUrl='" + fotoUrl + '\'' +
               ", dataCadastro=" + getDataCadastroFormatada() +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artista)) return false;
        Artista artista = (Artista) o;
        return Objects.equals(id, artista.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
