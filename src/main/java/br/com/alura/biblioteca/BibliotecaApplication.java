package br.com.alura.biblioteca;

import br.com.alura.biblioteca.principal.Principal;
import br.com.alura.biblioteca.repository.iAutorRepository;
import br.com.alura.biblioteca.repository.iLivrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner {
	@Autowired
	private iLivrosRepository livrosRepositorio;

	@Autowired
	private iAutorRepository autorRepositorio;

	//dom casmurro
	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livrosRepositorio, autorRepositorio);
		principal.exibeMenu();
	}
}