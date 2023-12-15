package View;
import Controller.EstoqueControll;
import Model.Produto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VendasPainel extends JPanel {

    private JTextField codigoBarrasField;
    private DefaultListModel<String> produtosListModel;
    private JList<String> produtosList;
    private JCheckBox clienteVipCheckBox;
    private JButton adicionarProdutoButton, removerProdutoButton, limparCarrinhoButton;
    private EstoqueControll estoqueControll;
    private JButton avancarButton;
    private JLabel totalLabel;
    private double total;
    private ConclusaoCompraPainel conclusaoCompraPainel;

    public VendasPainel(EstoqueControll estoqueControll, ConclusaoCompraPainel conclusaoCompraPainel) {
        this.estoqueControll = estoqueControll;
        this.conclusaoCompraPainel = conclusaoCompraPainel;
        setLayout(new BorderLayout());

        codigoBarrasField = new JTextField();
        adicionarProdutoButton = new JButton("Adicionar Produto");

        produtosListModel = new DefaultListModel<>();
        produtosList = new JList<>(produtosListModel);
        JScrollPane produtosScrollPane = new JScrollPane(produtosList);

        removerProdutoButton = new JButton("Remover Produto");
        clienteVipCheckBox = new JCheckBox("Cliente VIP");

        avancarButton = new JButton("Pagamento");
        totalLabel = new JLabel("Total: R$ 0.00");

        JPanel codigoBarrasPanel = new JPanel(new BorderLayout());
        codigoBarrasPanel.add(new JLabel("Código de Barras: "), BorderLayout.WEST);
        codigoBarrasPanel.add(codigoBarrasField, BorderLayout.CENTER);
        codigoBarrasPanel.add(adicionarProdutoButton, BorderLayout.EAST);

        JPanel botoesPanel = new JPanel();
        botoesPanel.add(removerProdutoButton);
        botoesPanel.add(clienteVipCheckBox);
        botoesPanel.add(avancarButton);
        botoesPanel.add(totalLabel);

        limparCarrinhoButton = new JButton("Limpar Carrinho");
        botoesPanel.add(limparCarrinhoButton);

        add(codigoBarrasPanel, BorderLayout.NORTH);
        add(produtosScrollPane, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);

        adicionarProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarProduto();
            }
        });

        removerProdutoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerProduto();
            }
        });

        avancarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para avançar para a tela de conclusão
                JTabbedPane jTPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class,
                        VendasPainel.this);
                jTPane.setSelectedIndex(2);

                // Obtém a referência para o ConclusaoCompraPainel
                ConclusaoCompraPainel conclusaoCompraPainel = (ConclusaoCompraPainel) jTPane.getComponentAt(2);

                // Obtém os produtos do VendasPainel
                List<String> produtos = obterProdutosDoVendasPainel();

                // Passa os produtos e o total para o ConclusaoCompraPainel
                conclusaoCompraPainel.setProdutos(produtos);
                conclusaoCompraPainel.setTotal(total);
            }
        });

        limparCarrinhoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCarrinho();
            }
        });
    }

    private List<String> obterProdutosDoVendasPainel() {
        List<String> produtos = new ArrayList<>();
        for (int i = 0; i < produtosListModel.size(); i++) {
            produtos.add(produtosListModel.getElementAt(i));
        }
        return produtos;
    }

    private void adicionarProduto() {
        String codigoBarras = codigoBarrasField.getText();
        if (!codigoBarras.isEmpty()) {
            Produto produto = estoqueControll.obterProdutoPorCodigoBarras(codigoBarras);

            if (produto != null) {
                double precoProduto = produto.getPreco();

                if (clienteVipCheckBox.isSelected()) {
                    precoProduto *= 0.95; // Desconto de 5%
                }

                produtosListModel.addElement(produto.getNome() + " - Preço: R$" + precoProduto);
                codigoBarrasField.setText("");

                total += precoProduto;
                atualizarTotalLabel();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado no estoque", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerProduto() {
        int selectedIndex = produtosList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selectedProduct = produtosListModel.getElementAt(selectedIndex);
            double preco = extrairPrecoDoTexto(selectedProduct);

            total -= preco;
            atualizarTotalLabel();

            produtosListModel.remove(selectedIndex);
        }
    }

    private double extrairPrecoDoTexto(String textoProduto) {
        String precoTexto = textoProduto.substring(textoProduto.lastIndexOf("R$") + 3).trim();
        return Double.parseDouble(precoTexto);
    }

    private void atualizarTotalLabel() {
        totalLabel.setText("Total: R$" + String.format("%.2f", total));
    }

    private void limparCarrinho() {
        produtosListModel.clear();
        total = 0.0;
        atualizarTotalLabel();
    }

    public void setTotal(double total) {
        this.total = total;
        atualizarTotalLabel();
    }
}
