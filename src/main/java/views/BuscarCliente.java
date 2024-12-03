package main.java.views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import main.java.models.Cliente;

public class BuscarCliente extends JFrame {

    private JTextField campoBusca;
    private List<Cliente> listaClientes;
    private DefaultTableModel modeloTabela;

    public BuscarCliente(List<Cliente> listaClientes, DefaultTableModel modeloTabela) {
        this.listaClientes = listaClientes;
        this.modeloTabela = modeloTabela;
        criarFormulario();
    }

    private void criarFormulario() {
        setTitle("Buscar Cliente");
        setSize(400, 150);
        setLayout(new BorderLayout());

        JPanel painelBusca = new JPanel(new GridLayout(2, 1));

        painelBusca.add(new JLabel("Digite o termo de busca (nome ou sobrenome):"));
        campoBusca = new JTextField();
        painelBusca.add(campoBusca);

        add(painelBusca, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        JButton btnBuscar = new JButton("Buscar");
        JButton btnCancelar = new JButton("Cancelar");

        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {  buscarCliente();  }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {  dispose(); }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buscarCliente() {
        String termoBusca = campoBusca.getText().trim();  // Obtém o termo buscado

        if (!termoBusca.isEmpty()) {
            termoBusca = termoBusca.toLowerCase();  // Converte o termo de busca para minusculo
            modeloTabela.setRowCount(0);  // Limpa a tabela antes de mostrar os resultados para evitar que resultados anteriores apareçam na tela
            int contador = 1;

            // Percorre a lista de clientes e busca o termo nas colunas
            for (Cliente cliente : listaClientes) {

                // Verifica se o termo buscado está em alguma coluna da tabela
                if (cliente.getNome().toLowerCase().contains(termoBusca) ||
                        cliente.getSobrenome().toLowerCase().contains(termoBusca)) {

                    // Adiciona os clientes encontrados a tabela
                    modeloTabela.addRow(new Object[]{contador++, cliente.getNome(), cliente.getSobrenome(), cliente.getEndereco(), cliente.getTelefone(), cliente.getCreditScore()});
                }
            }

            if (contador == 1) {
                JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, insira um nome ou sobrenome para busca.");
        }
    }
}