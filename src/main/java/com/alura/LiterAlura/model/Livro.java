package com.alura.LiterAlura.model;

import jakarta.persistence.*;

@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLivro;
    private String titulo;
    private String idioma;
    private Integer numeroDownloads;

    @ManyToOne
    @JoinColumn(name = "idAutor")
    private Autor autor;

    public Livro() {}

    public Livro(String titulo, String idioma, Integer numeroDownloads, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
        this.autor = autor;
    }

    public Long getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Long idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(Integer numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "\n" +
                "  --- LIVRO ---" + "\n" +
                "  Título: " + titulo + "\n" +
                "  Autor: " + autor.getNome() + "\n" +
                "  Idioma: " + idioma + "\n" +
                "  Número de downloads: " + numeroDownloads + "\n" +
                "  ---------------------\n";
    }
}
