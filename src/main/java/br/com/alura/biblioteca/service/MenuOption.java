package br.com.alura.biblioteca.service;

import br.com.alura.biblioteca.principal.Principal;

public enum MenuOption {
    BUSCAR_LIVRO(1, "Buscar livro por nome", Principal::buscarLivro),
    LISTAR_LIVROS(2, "Listar livros salvos", Principal::listarLivrosSalvos),
    LISTAR_AUTORES(3, "Listar autores salvos", Principal::listarAutoresSalvos),
    LISTAR_AUTORES_VIVOS(4, "Listar autores vivos em um determinado ano", Principal::listarAutoreVivosEmUmAno),
    LISTAR_LIVROS_IDIOMA(5, "Listar livros por idioma", Principal::listarLivrosPorIdioma),
    SAIR(0, "Sair", principal -> System.out.println("Encerrando a aplicação..."));

    private final int opcao;
    private final String descricao;
    private final MenuAction acao;

    MenuOption(int opcao, String descricao, MenuAction acao) {
        this.opcao = opcao;
        this.descricao = descricao;
        this.acao = acao;
    }

    public int getOpcao() {
        return opcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void executar(Principal principal) {
        acao.execute(principal);
    }

    public static MenuOption fromOpcao(int opcao) {
        for (MenuOption menuOption : values()) {
            if (menuOption.getOpcao() == opcao) {
                return menuOption;
            }
        }
        return null;
    }

    @FunctionalInterface
    interface MenuAction {
        void execute(Principal principal);
    }
}
