package br.com.alura.biblioteca.service;

public interface ICoverteDados {

    <T> T obterDados(String json, Class<T> classe);
}
