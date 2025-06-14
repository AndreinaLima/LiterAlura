package com.alura.LiterAlura.service;

public interface IConverteDados {
    <T> T converterDados(String response, Class<T> classe);
}

