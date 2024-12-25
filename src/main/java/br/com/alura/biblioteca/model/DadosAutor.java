package br.com.alura.biblioteca.model;


import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosAutor(@JsonAlias("name") String nome,
                         @JsonAlias("birth_year") Integer anoNascimento,
                         @JsonAlias("death_year") Integer anoFalecimento) {
}