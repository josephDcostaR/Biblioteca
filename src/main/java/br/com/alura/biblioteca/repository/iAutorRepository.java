package br.com.alura.biblioteca.repository;

import br.com.alura.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface iAutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeContains(String nome);
    List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(Integer anoNascimento, Integer anoFalecimento);
}