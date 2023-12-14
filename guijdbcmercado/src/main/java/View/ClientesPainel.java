package View;

import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Connection.ClientesDAO;
import Controller.ClientesControl;
import java.awt.*;
import java.awt.event.*;
import Model.Clientes;

public class ClientesPainel extends JPanel {
    // Atributos(componentes)
    private JButton cadastrar, apagar, editar;
    private JTextField clienteNomeField, clienteEnderecoField, clienteTelefoneTextField, clienteCpfField;
    private List<Clientes> clientes;
    private JTable table;
    private DefaultTableModel tableModel;
    private int linhaSelecionada = -1;

    // Construtor(GUI-JPanel)
    public ClientesPainel() {
        super();
        // entrada de dados
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Cadastro Clientes"));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Nome"));
        clienteNomeField = new JTextField(20);
        inputPanel.add(clienteNomeField);
        inputPanel.add(new JLabel("Endereço"));
        clienteEnderecoField = new JTextField(20);
        inputPanel.add(clienteEnderecoField);
        inputPanel.add(new JLabel("Telefone"));
        clienteTelefoneTextField = new JTextField(20);
        inputPanel.add(clienteTelefoneTextField);
        inputPanel.add(new JLabel("CPF"));
        clienteCpfField = new JTextField(20);
        inputPanel.add(clienteCpfField);

        add(inputPanel);
        JPanel botoes = new JPanel();
        botoes.add(cadastrar = new JButton("Cadastrar"));
        botoes.add(editar = new JButton("Editar"));
        botoes.add(apagar = new JButton("Apagar"));
        add(botoes);
        // tabela de produtos
        JScrollPane jSPane = new JScrollPane();
        add(jSPane);
        tableModel = new DefaultTableModel(new Object[][] {},
                new String[] { "Nome", "Endereço", "Telefone", "CPF"});
        table = new JTable(tableModel);
        jSPane.setViewportView(table);

        // // Cria o banco de dados caso não tenha sido criado
        // new ProdutosDAO().criaTabela();
        // // incluindo elementos do banco na criação do painel
        atualizarTabela();

        // tratamento de Eventos(construtor)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    clienteNomeField.setText((String) table.getValueAt(linhaSelecionada, 0));
                    clienteEnderecoField.setText((String) table.getValueAt(linhaSelecionada, 1));
                    clienteTelefoneTextField.setText((String) table.getValueAt(linhaSelecionada, 2));
                    clienteCpfField.setText((String) table.getValueAt(linhaSelecionada, 3));
                }
            }
        });
        // Cria um objeto operacoes da classe ProdutosControl para executar operações no
        // banco de dados
        ClientesControl operacoes = new ClientesControl(clientes, tableModel, table);

        // Configura a ação do botão "cadastrar" para adicionar um novo registro no
        // bancco de dados
        cadastrar.addActionListener(e -> {
            operacoes.cadastrar(clienteNomeField.getText(), clienteEnderecoField.getText(), clienteTelefoneTextField.getText(),
                    clienteCpfField.getText());
            // Limpa os campos de entrada após a operação de cadastro
            clienteNomeField.setText("");
            clienteEnderecoField.setText("");
            clienteTelefoneTextField.setText("");
            clienteCpfField.setText("");
        });
        // Configura a ação do botão "editar" para atualizar um registro no banco de
        // dados
        editar.addActionListener(e -> {
            operacoes.atualizar(clienteNomeField.getText(), clienteEnderecoField.getText(), clienteTelefoneTextField.getText(),
                    clienteCpfField.getText());
            // Limpa os campos de entrada após a operação de atualização
            clienteNomeField.setText("");
            clienteEnderecoField.setText("");
            clienteTelefoneTextField.setText("");
            clienteCpfField.setText("");
        });
        // Configura a ação do botão "apagar" para excluir um registro no banco de dados
        apagar.addActionListener(e -> {
            operacoes.apagar(clienteCpfField.getText());
            // Limpa os campos de entrada após a operação de exclusão
            clienteNomeField.setText("");
            clienteEnderecoField.setText("");
            clienteTelefoneTextField.setText("");
            clienteCpfField.setText("");
        });

        // Defina as novas cores para os componentes
        Color background = new Color(255, 255, 240); // Cor de fundo mais clara
        Color buttonColor = new Color(0, 102, 51); // Cor do botão verde-escuro
        Color labelColor = Color.BLACK; // Cor do texto preto
        Color tableHeaderColor = new Color(0, 51, 25); // Cor do cabeçalho da tabela

        setBackground(background);

        inputPanel.setBackground(background);
        inputPanel.setForeground(labelColor);

        clienteNomeField.setBackground(Color.WHITE);
        clienteEnderecoField.setBackground(Color.WHITE);
        clienteTelefoneTextField.setBackground(Color.WHITE);
        clienteCpfField.setBackground(Color.WHITE);

        cadastrar.setBackground(buttonColor);
        cadastrar.setForeground(Color.WHITE);

        editar.setBackground(buttonColor);
        editar.setForeground(Color.WHITE);

        apagar.setBackground(buttonColor);
        apagar.setForeground(Color.WHITE);

        table.setBackground(Color.WHITE);
        table.setForeground(labelColor);
        table.setSelectionBackground(new Color(204, 255, 204)); // Cor de seleção mais clara
        table.getTableHeader().setBackground(tableHeaderColor);
        table.getTableHeader().setForeground(Color.WHITE);
    }

    // metodos de eventos (atualizar tabela)
    // Método para atualizar a tabela de exibição com dados do banco de dados
    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa todas as linhas existentes na tabela
        clientes = new ClientesDAO().listarTodos();
        // Obtém os produtos atualizados do banco de dados
        for (Clientes cliente : clientes) {
            // Adiciona os dados de cada produto como uma nova linha na tabela Swing
            tableModel.addRow(new Object[] { cliente.getNome(), cliente.getEndereco(),
                    cliente.getTelefone(), cliente.getCpf() });
        }
    }
}
