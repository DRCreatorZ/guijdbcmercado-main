package View;

import javax.swing.*;
import Controller.EstoqueControll;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ConclusaoCompraPainel extends JPanel {

    private DefaultListModel<String> detalhesCompraModel;
    private JList<String> detalhesCompraList;
    private JLabel totalCompraLabel;
    private JComboBox<String> opcoesPagamentoComboBox;
    private JButton finalizarCompraButton, imprimirCupomButton;
    private double total;
    private EstoqueControll estoqueControll;

    public ConclusaoCompraPainel(EstoqueControll estoqueControll) {
        this.estoqueControll = estoqueControll;
        setLayout(new BorderLayout());

        detalhesCompraModel = new DefaultListModel<>();
        detalhesCompraList = new JList<>(detalhesCompraModel);
        JScrollPane detalhesCompraScrollPane = new JScrollPane(detalhesCompraList);

        totalCompraLabel = new JLabel("Total da Compra: R$ 0.00");
        opcoesPagamentoComboBox = new JComboBox<>(new String[]{"Dinheiro", "Cartão de Crédito", "Cartão de Débito", "Pix"});
        finalizarCompraButton = new JButton("Finalizar Compra");
        imprimirCupomButton = new JButton("Imprimir Cupom Fiscal");

        // Defina as novas cores para os componentes
        Color background = new Color(255, 255, 240); // Cor de fundo amarelo claro
        Color buttonColor = new Color(50, 120, 50); // Cor do botão verde escuro
        Color labelColor = Color.BLACK; // Cor do texto preto

        setBackground(background);

        detalhesCompraList.setBackground(Color.WHITE);
        detalhesCompraList.setSelectionBackground(new Color(200, 200, 255));

        totalCompraLabel.setForeground(labelColor);

        opcoesPagamentoComboBox.setBackground(Color.WHITE);
        opcoesPagamentoComboBox.setForeground(labelColor);

        finalizarCompraButton.setBackground(buttonColor);
        finalizarCompraButton.setForeground(Color.WHITE);

        imprimirCupomButton.setBackground(buttonColor);
        imprimirCupomButton.setForeground(Color.WHITE);

        JPanel botoesPanel = new JPanel();
        botoesPanel.setBackground(background);
        botoesPanel.add(finalizarCompraButton);
        botoesPanel.add(imprimirCupomButton);

        add(detalhesCompraScrollPane, BorderLayout.CENTER);
        add(totalCompraLabel, BorderLayout.SOUTH);
        add(opcoesPagamentoComboBox, BorderLayout.NORTH);
        add(botoesPanel, BorderLayout.EAST);

        finalizarCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarCompra();
            }
        });

        imprimirCupomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imprimirCupomFiscal();
            }
        });
    }

    // Métodos adicionados permanecem inalterados

    private void finalizarCompra() {
        JOptionPane.showMessageDialog(this, "Compra finalizada com sucesso!");

        for (int i = 0; i < detalhesCompraModel.size(); i++) {
            String produtoTexto = detalhesCompraModel.getElementAt(i);
            String codigoBarras = extrairCodigoBarrasDoTexto(produtoTexto);
            int quantidadeComprada = extrairQuantidadeDoTexto(produtoTexto);

            System.out.println("Produto: " + codigoBarras + ", Quantidade: " + quantidadeComprada);

            estoqueControll.deduzirQuantidadeDoEstoque(codigoBarras, quantidadeComprada);
        }
    }

    private String extrairCodigoBarrasDoTexto(String textoProduto) {
        if (textoProduto.matches("\\d+ - .*")) {
            return textoProduto.split(" - ")[0];
        } else {
            return "";
        }
    }

    private int extrairQuantidadeDoTexto(String textoProduto) {
        try {
            int ultimoEspaco = textoProduto.lastIndexOf(" ");
            String quantidadeTexto = textoProduto.substring(ultimoEspaco + 1);
            quantidadeTexto = quantidadeTexto.replaceAll("\\D+", "");
            return Integer.parseInt(quantidadeTexto);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void imprimirCupomFiscal() {
        String dataHoraAtual = java.time.LocalDateTime.now().toString();
        estoqueControll.imprimirCupomFiscal(total, dataHoraAtual, this);
    }

    public DefaultListModel<String> getDetalhesCompraModel() {
        return detalhesCompraModel;
    }

    public void setProdutos(List<String> produtos) {
        detalhesCompraModel.clear();
        for (String produto : produtos) {
            detalhesCompraModel.addElement(produto);
        }
    }

    public void setTotal(double total) {
        this.total = total;
        totalCompraLabel.setText("Total da Compra: R$" + String.format("%.2f", total));
    }
}
