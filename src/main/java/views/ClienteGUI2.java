package main.java.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import main.java.models.ArquivoCliente;
import main.java.models.BufferDeClientes;
import main.java.models.Cliente;

public class ClienteGUI2 extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private BufferDeClientes bufferDeClientes;
    private final int TAMANHO_BUFFER = 10000;
    private int registrosCarregados = 0; // Contador de registros já carregados
    private String arquivoSelecionado;
    private boolean arquivoCarregado = false; // Para verificar se o arquivo foi carregado

    private final List<Cliente> listaClientes;


    public ClienteGUI2() {
        setTitle("Gerenciamento de Clientes");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        bufferDeClientes = new BufferDeClientes();
        listaClientes = new ArrayList<>();
        criarInterface();
    }

    private void carregarArquivo() {
        JFileChooser fileChooser = new JFileChooser();
        int retorno = fileChooser.showOpenDialog(this);

        if (retorno == JFileChooser.APPROVE_OPTION) {
            arquivoSelecionado = fileChooser.getSelectedFile().getAbsolutePath();
            bufferDeClientes.associaBuffer(new ArquivoCliente()); // Substitua por sua implementação
            bufferDeClientes.inicializaBuffer("leitura", arquivoSelecionado); // Passa o nome do arquivo aqui
            registrosCarregados = 0; // Reseta o contador
            tableModel.setRowCount(0); // Limpa a tabela
            carregarMaisClientes(); // Carrega os primeiros clientes
            arquivoCarregado = true; // Marca que o arquivo foi carregado
        }
    }

    private void criarInterface() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel btn = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btnCarregar = new JButton("Carregar Clientes");
        JButton btnBuscar = new JButton("Buscar Cliente");
        JButton btnInserir = new JButton("Inserir Cliente");
        JButton btnRemover = new JButton("Remover Cliente");
        JButton btnRecarregar = new JButton("Recarregar");
        JButton btnAlfabeto = new JButton("Ordenar Alfabeticamente");

        tableModel = new DefaultTableModel(new String[]{"#", "Nome", "Sobrenome", "Telefone", "Endereço", "Credit Score"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Adiciona um listener ao JScrollPane para carregar mais clientes ao rolar
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!scrollPane.getVerticalScrollBar().getValueIsAdjusting()) {
                    // Verifica se estamos no final da tabela e se o arquivo foi carregado
                    if (arquivoCarregado &&
                            scrollPane.getVerticalScrollBar().getValue() +
                                    scrollPane.getVerticalScrollBar().getVisibleAmount() >=
                                    scrollPane.getVerticalScrollBar().getMaximum()) {
                        carregarMaisClientes();
                    }
                }
            }
        });

        // Ações dos botões
        btnCarregar.addActionListener(_ -> carregarArquivo());
        btnBuscar.addActionListener(_ -> new BuscarCliente(listaClientes, tableModel));
        btnInserir.addActionListener(_ -> new InserirCliente(listaClientes, tableModel));
        // btnRemover.addActionListener(_ -> removerCliente());
        // btnRecarregar.addActionListener(_ -> mostrarTodosClientes());
        // btnAlfabeto.addActionListener(_ -> ordenarAlfabeticamente(listaClientes, tableModel));

        btn.add(btnCarregar);
        btn.add(btnBuscar);
        btn.add(btnInserir);
        btn.add(btnRemover);
        btn.add(btnRecarregar);
        btn.add(btnAlfabeto);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);
        add(panel);
    }

    private void carregarMaisClientes() {
        // Carrega apenas 10.000 registros de cada vez
        Cliente[] clientes = bufferDeClientes.proximosClientes(TAMANHO_BUFFER); // Chama o método com o tamanho do buffer
        if (clientes != null && clientes.length > 0) {
            for (Cliente cliente : clientes) {
                if (cliente != null) { // Verifica se o cliente não é nulo
                    tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, cliente.getNome(), cliente.getSobrenome(), cliente.getTelefone(), cliente.getEndereco(), cliente.getCreditScore()});
                }
            }
            registrosCarregados += clientes.length; // Atualiza o contador
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClienteGUI2 gui = new ClienteGUI2();
            gui.setVisible(true);
        });
    }
}