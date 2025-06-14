package com.alura.LiterAlura.principal;

import com.alura.LiterAlura.model.*;
import com.alura.LiterAlura.repository.*;
import com.alura.LiterAlura.service.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    String menu = """
            ---------------------------------------------------------------------------
            ---------------------------------------------------------------------------
            Escolha a opção a executar através de um número:
            1 - Buscar e registrar livro digitando o título ou parte do título.
            2 - Listar livros registrados.
            3 - Listar autores registrados.
            4 - Listar autores vivos em um determinado ano.
            5 - Listar livros por idioma.
            6 - Sair.
            """;

    public static final String PUBLIC_API = "https://gutendex.com/books";
    private ConverteDados converteDados = new ConverteDados();
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private AutorRepository repositoryAutor;
    private LivroRepository repositoryLivro;

    public Principal(AutorRepository repositoryAutor, LivroRepository repositoryLivro) {
        this.repositoryAutor = repositoryAutor;
        this.repositoryLivro = repositoryLivro;
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 6) {
            System.out.println(menu);
            System.out.print("Escolha uma opção: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                switch (opcao) {
                    case 1:
                        buscarRegistrarLivroPorNome();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosAteAnoIndicado();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 6:
                        System.out.println("Encerrando aplicação...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } else {
                System.out.println("Escolha uma opção válida por favor. \nTente novamente.");
                scanner.nextLine();
            }
        }
    }

    private void buscarRegistrarLivroPorNome() {
        System.out.print("Digite o título ou parte do título do livro que deseja buscar/registrar: ");
        Scanner scanner = new Scanner(System.in);
        String buscaLivro = scanner.nextLine();
        String response = consumoAPI.obterDados(PUBLIC_API + "?search=" + buscaLivro.replace(" ", "%20"));
        var dadosBusca = converteDados.converterDados(response, DadosLivros.class);

        Optional<DadosLivro> livroBuscado = dadosBusca.results().stream()
                .filter(e -> e.title().toUpperCase().contains(buscaLivro.toUpperCase()))
                .findFirst();

        if (livroBuscado.isPresent()) {
            DadosLivro dados = livroBuscado.get();
            DadosAutor dadosAutor = dados.authors().isEmpty() ? null : dados.authors().get(0);
            String idioma = dados.languages().isEmpty() ? "" : dados.languages().get(0);

            Optional<Autor> autorExistente = repositoryAutor.findByNome(dadosAutor.name());
            Autor autor = autorExistente.orElseGet(() ->
                    repositoryAutor.save(new Autor(dadosAutor.name(), dadosAutor.birth_year(), dadosAutor.death_year()))
            );

            Optional<Livro> livroExistente = repositoryLivro.findByTitulo(dados.title());
            if (livroExistente.isEmpty()) {
                Livro livro = new Livro(dados.title(), idioma, dados.download_count(), autor);
                Livro livroSalvo = repositoryLivro.save(livro);
                System.out.println(livroSalvo);
            } else {
                System.out.println("Esse livro já está registrado no banco de dados.");
            }
        } else {
            System.out.println("Livro não encontrado.");
        }
    }

    public void listarLivrosRegistrados() {
        List<Livro> lista = repositoryLivro.findAll();
        if (lista != null && !lista.isEmpty()) {
            lista.forEach(System.out::println);
        } else {
            System.out.println("Nenhum livro registrado.");
        }
    }

    private void imprimirLivrosPorAutor(Autor autor) {
        List<Livro> livros = autor.getLivros();
        if (livros != null && !livros.isEmpty()) {
            for (Livro livro : livros) {
                System.out.println("    - " + livro.getTitulo());
            }
        } else {
            System.out.println("    - Nenhum livro registrado para este autor.");
        }
    }

    public void listarAutoresRegistrados() {
        List<Autor> lista = repositoryAutor.findAll();
        if (lista != null && !lista.isEmpty()) {
            for (Autor autor : lista) {
                System.out.println();
                System.out.println("  --- AUTOR ---");
                System.out.println("  Nome: " + autor.getNome());
                System.out.println("  Ano de nascimento: " + autor.getAnoNascimento());
                System.out.println("  Ano de falecimento: " + autor.getAnoFalecimento());
                System.out.println("  Livros:");
                imprimirLivrosPorAutor(autor);
                System.out.println("  ---------------------\n");
            }
        } else {
            System.out.println("Nenhum autor registrado.");
        }
    }

    public void listarAutoresVivosAteAnoIndicado() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o ano: ");
        if (scanner.hasNextInt()) {
            int ano = scanner.nextInt();
            List<Autor> autoresVivos = repositoryAutor.listarAutoresVivosPorAno(ano);
            if (autoresVivos != null && !autoresVivos.isEmpty()) {
                for (Autor autor : autoresVivos) {
                    System.out.println();
                    System.out.println("  --- AUTOR ---");
                    System.out.println("  Nome: " + autor.getNome());
                    System.out.println("  Ano de nascimento: " + autor.getAnoNascimento());
                    System.out.println("  Ano de falecimento: " + autor.getAnoFalecimento());
                    System.out.println("  Livros:");
                    imprimirLivrosPorAutor(autor);
                    System.out.println("  ---------------------\n");
                }
            } else {
                System.out.println("Nenhum autor vivo encontrado para esse ano.");
            }
        } else {
            System.out.println("Insira um valor válido por favor. \nTente novamente.");
            listarAutoresVivosAteAnoIndicado();
        }
    }

    public void listarLivrosPorIdioma() {
        Scanner scanner = new Scanner(System.in);
        String menuIdiomas = """
            es - Espanhol
            en - Inglês
            Escolha o idioma:
            """;
        System.out.print(menuIdiomas);
        try {
            String idiomaEscolhido = scanner.nextLine();
            List<Livro> livrosEncontrados = repositoryLivro.findByIdioma(idiomaEscolhido);
            if (livrosEncontrados != null && !livrosEncontrados.isEmpty()) {
                for (Livro livro : livrosEncontrados) {
                    System.out.println(livro);
                }
            } else {
                System.out.println("Nenhum livro encontrado com esse idioma.");
            }
        } catch (Exception e) {
            System.out.println("Algo deu errado. Tente novamente.");
            listarLivrosPorIdioma();
        }
    }
}