package com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(
        String name,
        Integer birth_year,
        Integer death_year
) {
}
