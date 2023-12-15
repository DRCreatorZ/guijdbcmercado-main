package View;

import Controller.EstoqueControll;

import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;
    private EstoqueControll gerenciadorEstoque;
    private JTabbedPane jTPane;

    public JanelaPrincipal() {
        super("Mini-Mercado D & M.");
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Inicializa o CardLayout
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Inicializa o EstoqueControll
        gerenciadorEstoque = new EstoqueControll();

        // Cria instâncias das duas outras janelas
        ConclusaoCompraPainel conclusaoCompraPainel = new ConclusaoCompraPainel(gerenciadorEstoque);
        VendasPainel vendasPainel = new VendasPainel(gerenciadorEstoque, conclusaoCompraPainel);

        // Adiciona as duas janelas ao CardLayout
        cards.add(conclusaoCompraPainel, "ConclusaoCompra");
        cards.add(vendasPainel, "Vendas");

        // Adiciona o CardLayout à janela principal
        add(cards);

        // Inicializa o JTabbedPane
        jTPane = new JTabbedPane();
        add(jTPane);

        // Cria instâncias das outras janelas
        ClientesPainel tab1 = new ClientesPainel();
        EstoquePainel tab4 = new EstoquePainel(gerenciadorEstoque);
        TelaInicial telaInicial = new TelaInicial();
        // Adiciona as janelas ao JTabbedPane
        jTPane.add("Tela Inicial", telaInicial);
        jTPane.add("Clientes", tab1);
        jTPane.add("Registro de Venda", vendasPainel);
        jTPane.add("Conclusão de Compras", conclusaoCompraPainel);
        jTPane.add("Estoque", tab4);
        

        // Define a janela principal para ser visível
        setVisible(true);

        // Defina as novas cores para a interface principal
        Color background = new Color(240, 240, 240); // Cor de fundo mais clara
        Color tabbedPaneBackground = new Color(200, 200, 200); // Cor de fundo do JTabbedPane
        Color tabbedPaneForeground = Color.BLACK; // Cor do texto do JTabbedPane

        getContentPane().setBackground(background);
        jTPane.setBackground(tabbedPaneBackground);
        jTPane.setForeground(tabbedPaneForeground);
    }

    // Método para obter o EstoqueControll
    public EstoqueControll getGerenciadorEstoque() {
        return gerenciadorEstoque;
    }

    // Método para iniciar a execução da janela
    public void run() {
        // Implemente aqui qualquer lógica de inicialização necessária
    }

    // Método para adicionar a guia de estoque à janela principal
    public void adicionarTabEstoque(EstoquePainel janelaEstoque) {
        cards.add(janelaEstoque, "Estoque");
    }

    // Método para abrir a janela de estoque separadamente
    public void abrirJanelaEstoque() {
        EstoquePainel estoqueJanela = new EstoquePainel(gerenciadorEstoque);

        JFrame estoqueFrame = new JFrame("Estoque");
        estoqueFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        estoqueFrame.setSize(400, 300);
        estoqueFrame.setLocationRelativeTo(null);
        estoqueFrame.add(estoqueJanela);
        estoqueFrame.setVisible(true);
    }

    // Método para definir o EstoqueControll
    public void setGerenciadorEstoque(EstoqueControll gerenciadorEstoque) {
        this.gerenciadorEstoque = gerenciadorEstoque;
    }
}
