package Model;


// Exemplo de classe de Produto no pacote Model
public class ProdutoView {
    private String codigoBarras;
    private String nome;
    private double preco;
    private int quantidadeEstoque;
    // Outros atributos e métodos relevantes

    public ProdutoView(String codigoBarras, String nome, double preco, int quantidadeEstoque) {
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        // Inicialize outros atributos conforme necessário
    }

    // Adicione getters, setters e outros métodos conforme necessário
}
