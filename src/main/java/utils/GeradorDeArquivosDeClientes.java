package main.java.utils;

import com.github.javafaker.Faker;
import main.java.models.ArquivoCliente;
import main.java.models.Cliente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.*;

public class GeradorDeArquivosDeClientes {

    private final ArquivoCliente arquivoCliente;
    private final Faker faker;
    private final Set<String> nomesGerados; // Set para armazenar nomes únicos

    // Construtor que inicializa o gerador de arquivos de clientes
    public GeradorDeArquivosDeClientes() {
        this.arquivoCliente = new ArquivoCliente();
        this.faker = new Faker();
        this.nomesGerados = new HashSet<>(); // Inicializa o conjunto de nomes gerados
    }

    // Método que gera um arquivo binário com um número especificado de registros de clientes
    public void gerarArquivoClientes(String nomeArquivo, int quantidadeClientes) {
        try {
            // Abrindo o arquivo no modo escrita
            arquivoCliente.abrirArquivo(nomeArquivo, "escrita", Cliente.class);

            // Lista para armazenar os clientes gerados
            List<Cliente> clientes = new ArrayList<>();

            for (int i = 0; i < quantidadeClientes; i++) {
                // Gerando um cliente fictício
                Cliente cliente = gerarClienteFicticio();
                clientes.add(cliente);
            }

            // Escrevendo a lista de clientes no arquivo
            arquivoCliente.escreveNoArquivo(clientes);

            // Fechando o arquivo
            arquivoCliente.fechaArquivo();
            System.out.println("Arquivo de clientes gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para gerar um cliente fictício
    private Cliente gerarClienteFicticio() {
        String nomeCompleto = gerarNomeUnico();
        String sobrenome = faker.name().lastName();
        String endereco = faker.address().fullAddress();
        String telefone = faker.phoneNumber().cellPhone();
        int creditScore = faker.number().numberBetween(0, 100);

        return new Cliente(nomeCompleto, sobrenome, endereco, telefone, creditScore);
    }

    // Método para gerar um nome único
    private String gerarNomeUnico() {
        String nomeCompleto;
        do {
            String nome = faker.name().firstName();
            String sobrenome = faker.name().lastName();
            nomeCompleto = nome + " " + sobrenome; // Nome completo (primeiro nome + sobrenome)
        } while (nomesGerados.contains(nomeCompleto)); // Verifica se o nome já foi gerado

        nomesGerados.add(nomeCompleto); // Adiciona o nome único ao conjunto
        return nomeCompleto;
    }

    // Método que gera um grande arquivo de clientes com até 100 milhões de registros
    public void geraGrandeDataSetDeClientes(String nomeArquivo, int quantidadeClientes) {
        // Definindo o tamanho de cada lote de clientes a ser gravado no arquivo
        final int TAMANHO_LOTE = 1_000_000; // 1 milhão de clientes por lote

        try {
            // Abrir o arquivo no modo escrita
            arquivoCliente.abrirArquivo(nomeArquivo, "escrita", Cliente.class);

            // Gerando os clientes em lotes
            int totalClientesGerados = 0;
            while (totalClientesGerados < quantidadeClientes) {
                // Calcula quantos clientes restam a serem gerados
                int clientesRestantes = quantidadeClientes - totalClientesGerados;
                int tamanhoDoLote = Math.min(TAMANHO_LOTE, clientesRestantes);

                // Lista para armazenar o lote atual de clientes
                List<Cliente> clientes = new ArrayList<>();

                // Gerando um lote de clientes fictícios
                for (int i = 0; i < tamanhoDoLote; i++) {
                    Cliente cliente = gerarClienteFicticio();
                    clientes.add(cliente);
                }

                // Escrevendo o lote de clientes no arquivo
                arquivoCliente.escreveNoArquivo(clientes);
                totalClientesGerados += tamanhoDoLote;

                System.out.println("Gerados e gravados " + totalClientesGerados + " clientes até agora.");
            }

            // Fechando o arquivo
            arquivoCliente.fechaArquivo();
            System.out.println("Arquivo com " + quantidadeClientes + " clientes gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método de teste para gerar um arquivo de clientes com 10.000 registros
    public static void testeGeracaoClientes() {
        Scanner scanner = new Scanner(System.in);

        // Solicitando o nome do arquivo ao usuário
        System.out.print("Digite o nome do arquivo de saída: ");
        String nomeArquivo = scanner.next();

        // Quantidade de clientes a serem gerados
        int quantidadeClientes = 10000;

        // Instanciando o gerador e gerando o arquivo de clientes
        GeradorDeArquivosDeClientes gerador = new GeradorDeArquivosDeClientes();
        gerador.gerarArquivoClientes(nomeArquivo, quantidadeClientes);
    }

    // Método principal para executar o gerador de um grande dataset
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitando o nome do arquivo ao usuário
        System.out.print("Digite o nome do arquivo de saída: ");
        String nomeArquivo = scanner.next();

        // Solicitando a quantidade de clientes ao usuário
        System.out.print("Digite a quantidade de clientes a serem gerados: ");
        int quantidadeClientes = scanner.nextInt();

        // Instanciando o gerador
        GeradorDeArquivosDeClientes gerador = new GeradorDeArquivosDeClientes();

        // Gerar um grande dataset de clientes
        gerador.geraGrandeDataSetDeClientes(nomeArquivo, quantidadeClientes);
    }
}
