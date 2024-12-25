package br.com.alura.biblioteca.principal;

import br.com.alura.biblioteca.model.Autor;
import br.com.alura.biblioteca.model.DadosLivro;
import br.com.alura.biblioteca.model.Livro;
import br.com.alura.biblioteca.model.Results;
import br.com.alura.biblioteca.repository.iAutorRepository;
import br.com.alura.biblioteca.repository.iLivrosRepository;
import br.com.alura.biblioteca.service.ConsumoAPI;
import br.com.alura.biblioteca.service.ConverteDados;
import br.com.alura.biblioteca.service.MenuOption;

import java.util.*;

public class Principal {
    private Scanner scan = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private iLivrosRepository livrosRepositorio;
    private iAutorRepository autorRepositorio;
    private static String API_URL = "https://gutendex.com/books/?search=";

    List<Livro> livros;
    List<Autor> autor;

    public Principal(iLivrosRepository livrosRepositorio, iAutorRepository autorRepositorio) {
        this.livrosRepositorio = livrosRepositorio;
        this.autorRepositorio = autorRepositorio;
    }


    public void exibeMenu() {
        int opcao = -1;
        while (opcao != 0) {
            exibirOpcoes();

            try {
                opcao = scan.nextInt();
                scan.nextLine();

                MenuOption menuOption = MenuOption.fromOpcao(opcao);
                if (menuOption != null) {
                    menuOption.executar(this);
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: insira um número válido.");
                scan.nextLine();
            }
        }
    }

    private void exibirOpcoes() {
        System.out.println("""
                |***************************************************|
                |*****                BEM-VINDO               ******|
                |***************************************************|
                
                """);
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("%d - %s%n", option.getOpcao(), option.getDescricao());
        }
        System.out.println("""
                
                |***************************************************|
                |*****            ESCOLHA UMA OPÇÂO           ******|
                |***************************************************|
                """);
    }

    public void listarLivrosPorIdioma() {
        System.out.println("lista de livros por idioma\n------------");
        System.out.println("""
                \n\t---- escolha o idioma ----
                \ten - inglês
                \tes - espanhol
                \tfr - francês
                \tpt - português
                """);
        String idioma = scan.nextLine();
        livros = livrosRepositorio.findByIdiomasContains(idioma);
        if (livros.isEmpty()) {
            System.out.println("livros pelo idioma escolhido não encontrado");
            listarLivrosPorIdioma();
        } else {
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    public void listarAutoreVivosEmUmAno() {
        System.out.println("liste os autores vivos em um determinado ano... por favor, insira o ano");
        Integer ano = Integer.valueOf(scan.nextLine());
        autor = autorRepositorio
                .findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);
        if (autor.isEmpty()) {
            System.out.println("autores vivos não encontrados");
        } else {
            autor.stream()
                    .sorted(Comparator.comparing(Autor::getNome))
                    .forEach(System.out::println);
        }
    }

    public void listarAutoresSalvos() {
        System.out.println("lista de autores no banco de dados\n------------");
        autor = autorRepositorio.findAll();
        autor.stream()
                .sorted(Comparator.comparing(Autor::getNome))
                .forEach(System.out::println);
    }

    public void listarLivrosSalvos() {
        System.out.println("lista de livros no banco de dados\n------------");
        livros = livrosRepositorio.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    public void buscarLivro() {
        System.out.println("qual livro deseja buscar?: ");
        var nomeLivro = scan.nextLine().toLowerCase();
        var json = consumo.obterDados(API_URL + nomeLivro.replace(" ", "%20").trim());
        var dados = conversor.obterDados(json, Results.class);
        if (dados.results().isEmpty()) {
            System.out.println("livro não encontrado");;
        } else {
            DadosLivro dadosLivro = dados.results().get(0);
            Livro livro = new Livro(dadosLivro);
            Autor autor = new Autor().pegaAutor(dadosLivro);
            salvarDados(livro, autor);
        }
    }

    private void salvarDados(Livro livro, Autor autor) {
        Optional<Livro> livroEncontrado = livrosRepositorio.findByTituloContains(livro.getTitulo());
        if (livroEncontrado.isPresent()) {
            System.out.println("esse livro já existe no banco de dados");
            System.out.println(livro.toString());
        } else {
            try {
                livrosRepositorio.save(livro);
                System.out.println("livro guardado");
                System.out.println(livro);
            } catch (Exception e) {
                System.out.println("erro: " + e.getMessage());
            }
        }

        Optional<Autor> autorEncontrado = autorRepositorio.findByNomeContains(autor.getNome());
        if (autorEncontrado.isPresent()) {
            System.out.println("esse autor já existe no banco de dados");
            System.out.println(autor.toString());
        } else {
            try {
                autorRepositorio.save(autor);
                System.out.println("autor guardado");
                System.out.println(autor);
            } catch (Exception e) {
                System.out.println("erro: " + e.getMessage());
            }
        }
    }

}