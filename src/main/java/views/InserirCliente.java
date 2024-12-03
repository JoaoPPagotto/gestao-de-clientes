package main.java.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import main.java.models.Cliente;
import main.java.interfaces.Buffer;

// Criando um formulário básico para inserção de novos clientes

public class InserirCliente extends JFrame {

    private JTextField campoNome;
    private JTextField campoSobrenome;
    private JTextField campoEndereco;
    private JTextField campoTelefone;
    private JTextField campoCreditScore;
    private List<Cliente> listaClientes;
    private DefaultTableModel modeloTabela;

    public InserirCliente(List<Cliente> listaClientes, DefaultTableModel modeloTabela) {
        this.listaClientes = listaClientes;
        this.modeloTabela = modeloTabela;
        criarFormulario();
    }

    private void criarFormulario() {
        setTitle("Inserir Novo Cliente");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2));

        // Campos do formulário
        add(new JLabel("Nome:"));
        campoNome = new JTextField();
        add(campoNome);

        add(new JLabel("Sobrenome:"));
        campoSobrenome = new JTextField();
        add(campoSobrenome);

        add(new JLabel("Endereço:"));
        campoEndereco = new JTextField();
        add(campoEndereco);

        add(new JLabel("Telefone:"));
        campoTelefone = new JTextField();
        add(campoTelefone);

        add(new JLabel("CreditScore:"));
        campoCreditScore = new JTextField();
        add(campoCreditScore);

        // Botão para adicionar cliente
        JButton btnAdicionar = new JButton("Adicionar Cliente");
        add(btnAdicionar);

        // Botão para cancelar a inserção
        JButton btnCancelar = new JButton("Cancelar");
        add(btnCancelar);

        // Ação do botão adicionar
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarCliente();
            }
        });

        // Ação do botão cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Fecha o formulário
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void adicionarCliente() {

        String nome = campoNome.getText();
        String sobrenome = campoSobrenome.getText();
        String endereco = campoEndereco.getText();
        String telefone = campoTelefone.getText();
        String creditScoreStr = campoCreditScore.getText();

        try {
            int creditScore = Integer.parseInt(creditScoreStr);

            // Cria o novo cliente e o adiciona na lista de clientes
            Cliente novoCliente = new Cliente(nome, sobrenome, endereco, telefone, creditScore);
            listaClientes.add(novoCliente);

            // Atualiza a tabela de clientes no ClienteGUI
            modeloTabela.addRow(new Object[]{listaClientes.size(), nome, sobrenome, endereco, telefone, creditScore});

            // Exibe mensagem de sucesso e fecha o formulário
            JOptionPane.showMessageDialog(this, "Cliente inserido com sucesso!");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "CreditScore inválido. Por favor, insira um número.");
        }
    }
}
