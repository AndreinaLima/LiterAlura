package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(
        String title,
        List<DadosAutor> authors,
        List<String> languages,
        Integer download_count
) {
    @Override
    public String toString() {
        return "  --- LIVRO ---" + "\n" +
                "  Título: " + title + "\n" +
                "  Autor(es): " + authors + "\n" +
                "  Idioma(s): " + languages + "\n" +
                "  Número de downloads: " + download_count + "\n" +
                "  ------------\n";
    }
}
