package com.alura.LiterAlura.service;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T converterDados(String response, Class<T> classe) {
        try {
            return mapper.readValue(response, classe);
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }
}
