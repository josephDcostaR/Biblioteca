package br.com.alura.biblioteca.repository;

import br.com.alura.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface iLivrosRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByTituloContains(String tirulo);
    List<Livro> findByIdiomasContains(String idiomas);
}