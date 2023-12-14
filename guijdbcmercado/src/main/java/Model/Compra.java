
package Model;

// Exemplo de classe de Compra no pacote Model
import java.util.List;

public class Compra {
    private List<Produto> produtosComprados;
    // Outros atributos e métodos relevantes

    public Compra(List<Produto> produtosComprados) {
        this.produtosComprados = produtosComprados;
        // Inicialize outros atributos conforme necessário
    }

    public List<Produto> getProdutosComprados() {
        return produtosComprados;
    }

    // Adicione outros getters, setters e métodos conforme necessário
}
