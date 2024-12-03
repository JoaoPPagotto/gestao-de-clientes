package main.java.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import main.java.models.ArquivoCliente;
import main.java.models.BufferDeClientes;
import main.java.models.Cliente;

public class ClienteGUI {

    private final BufferDeClientes bufferDeClientes;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;
    private final List<Cliente> listaClientes;

    public ClienteGUI() {
        bufferDeClientes = new BufferDeClientes();
        listaClientes = new ArrayList<>();
        criarInterface();
    }

    private void criarInterface() {
        JFrame frame = new JFrame("Gerenciador de Clientes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        // Painel de controle
        JPanel painelControle = new JPanel();
        JButton btnCarregar = new JButton("Carregar Clientes");
        JButton btnBuscar = new JButton("Buscar Cliente");
        JButton btnInserir = new JButton("Inserir Cliente");
        JButton btnRemover = new JButton("Remover Cliente");
        JButton btnRecarregar = new JButton("Recarregar");

        painelControle.add(btnCarregar);
        painelControle.add(btnBuscar);
        painelControle.add(btnInserir);
        painelControle.add(btnRemover);
        painelControle.add(btnRecarregar);

        frame.add(painelControle, BorderLayout.SOUTH);

        // Modelo da tabela
        modeloTabela = new DefaultTableModel(new Object[]{"#", "Nome", "Sobrenome", "Endereço", "Telefone", "CreditScore"}, 0);
        tabelaClientes = new JTable(modeloTabela) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede a edição das células
            }
        };

        // Ajusta a largura da primeira coluna
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(10);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Ação dos botões
        btnCarregar.addActionListener(_ -> carregarClientes());
        btnBuscar.addActionListener(_ -> new BuscarCliente(listaClientes, modeloTabela));
        btnInserir.addActionListener(_ -> new InserirCliente(listaClientes, modeloTabela));
        btnRemover.addActionListener(_ -> removerCliente());
        btnRecarregar.addActionListener(_ -> mostrarTodosClientes());

        frame.setVisible(true);
    }

    private void carregarClientes() {
        // Solicita o nome do arquivo
        String nomeArquivo = JOptionPane.showInputDialog(null, "Digite o nome do arquivo de clientes:");

        while (nomeArquivo != null && !nomeArquivo.trim().isEmpty()) {

            try {
                // Inicializa o buffer e carrega os dados usando models.ArquivoCliente
                bufferDeClientes.associaBuffer(new ArquivoCliente());
                bufferDeClientes.inicializaBuffer("leitura", nomeArquivo);

                modeloTabela.setRowCount(0); // Limpa a tabela antes de carregar novos dados
                listaClientes.clear();      // Limpa a lista

                // Lê os clientes do buffer e adiciona à tabela
                Cliente cliente;
                int contador = 1; // Contador de linhas

                while ((cliente = bufferDeClientes.proximoCliente()) != null) {
                    listaClientes.add(cliente);     // Armazenando os clientes na lista
                    modeloTabela.addRow(new Object[]{contador++, cliente.getNome(), cliente.getSobrenome(), cliente.getEndereco(), cliente.getTelefone(), cliente.getCreditScore()});
                }
                bufferDeClientes.fechaBuffer(); // Fecha o buffer
                break; // Sai do loop caso o arquivo tenha sido devidamente carregado

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar o arquivo.");
                break;
            }
        }
    }

    // Adicionando funcionalidade de remoção de cliente

    private void removerCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();

        if (selectedRow >= 0) {
            int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o cliente?", "Remover Cliente", JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                listaClientes.remove(selectedRow);  // Remove da lista de clientes
                modeloTabela.removeRow(selectedRow);  // Remove da tabela
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione um cliente para remover.");
        }
    }

    // Método que recarregar a lista original de clientes sem ordenação
    private void mostrarTodosClientes() {
        modeloTabela.setRowCount(0);  // Limpa a tabela

        int contador = 1;
        for (Cliente cliente : listaClientes) {
            modeloTabela.addRow(new Object[]{contador++, cliente.getNome(), cliente.getSobrenome(), cliente.getEndereco(), cliente.getTelefone(), cliente.getCreditScore()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClienteGUI::new);
    }
}